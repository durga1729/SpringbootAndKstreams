package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.producer.KafkaProducer;
import com.example.demo.service.KafkaStreamService;
import lombok.AllArgsConstructor;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class EmployeeController {

    private final StreamsBuilderFactoryBean factoryBean;

    private final KafkaProducer kafkaProducer;

    @Autowired
    private final KafkaStreamService kafkaStreamService;

    @PostMapping("/emp")
    public void addEmployee(@RequestBody Employee employee) {
        kafkaProducer.sendEmpMessage(employee);
    }

    @GetMapping("/all")
    public List<Employee> all() {
        List<Employee> orders = new ArrayList<>();
        ReadOnlyKeyValueStore<String, Employee> store = factoryBean
                .getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(
                        "employees",
                        QueryableStoreTypes.keyValueStore()));
        KeyValueIterator<String, Employee> it = store.all();
        it.forEachRemaining(kv -> orders.add(kv.value));
        return orders;
    }
}
