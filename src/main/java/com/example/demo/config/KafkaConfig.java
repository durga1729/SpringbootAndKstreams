package com.example.demo.config;

import com.example.demo.model.Employee;
import com.example.demo.serde.EmployeeSerde;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;



  /*  @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(APPLICATION_ID_CONFIG, "streams-app");
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
        // configure the state location to allow tests to use clean state for every run
        props.put(STATE_DIR_CONFIG, "src/main/resources");
      //  props.put("schema.registry.url", "http://schema-registry11:8081");
        return new KafkaStreamsConfiguration(props);
    }*/

    @Bean
    NewTopic inputTopic() {
        return TopicBuilder.name("input-topic")
            .partitions(1)
            .replicas(1)
            .build();
    }

    @Bean
    NewTopic empTopic() {
        return TopicBuilder.name("emp-in-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }



    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
        // Define the source stream
        KStream<String, String> stream = streamsBuilder.stream("input-topic");

        // Process the stream (e.g., convert values to uppercase)
        KStream<String, String> processedStream = stream.mapValues(value -> new StringBuilder(value).reverse().toString());

        // Send the processed data to an output topic with transactional support
        processedStream.to("output-topic");

        return stream;
    }


    @Bean
    public KStream<String, Employee> empStream(StreamsBuilder streamsBuilder) {

     //   JsonSerde<Employee> employeeJsonSerde = new JsonSerde<>(Employee.class);
 //  ObjectMapper objectMapper = new ObjectMapper();

        Serde<Employee> employeeSerde = new EmployeeSerde();
        // Define the source stream
        KStream<String, Employee> stream = streamsBuilder.stream("emp-in-topic", Consumed.with(Serdes.String(),employeeSerde));

        // Process the stream (e.g., convert values to uppercase)
        KStream<String, Employee> processedStream = stream.filter(
                (key, employee) -> employee.getSalary() > 10000);


        //processedStream.k
        // Send the processed data to an output topic with transactional support
        processedStream.to("emp-out-topic", Produced.with(Serdes.String(), employeeSerde));

        return stream;
    }

    @Bean
    public KTable<String, Employee> table(StreamsBuilder builder) {
        KeyValueBytesStoreSupplier store =
                Stores.persistentKeyValueStore("employees");
        JsonSerde<Employee> orderSerde = new JsonSerde<>(Employee.class);
        KStream<String, Employee> stream = builder
                .stream("emp-in-topic", Consumed.with(Serdes.String(), orderSerde));
        return stream.toTable(Materialized.<String, Employee>as(store)
                .withKeySerde(Serdes.String())
                .withValueSerde(orderSerde));
    }

}