package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.producer.KafkaProducer;
import com.example.demo.service.KafkaStreamService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.*;

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
}
