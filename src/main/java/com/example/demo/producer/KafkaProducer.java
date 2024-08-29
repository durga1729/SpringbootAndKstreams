package com.example.demo.producer;

import com.example.demo.model.Employee;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Employee> kafkaTemplate;

    public void sendMessage(String message) {
       /* kafkaTemplate.send("input-topic", message)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Message sent to topic: {}", message);
                } else {
                    log.error("Failed to send message", ex);
                }
            });*/
    }

    public void sendEmpMessage(Employee message) {
       // if (message instanceof Employee) {
            kafkaTemplate.send("emp-in-topic", String.valueOf(message.getId()), message)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Message sent to topic: {}", message);
                        } else {
                            log.error("Failed to send message", ex);
                        }
                    });
        //}
    }
}