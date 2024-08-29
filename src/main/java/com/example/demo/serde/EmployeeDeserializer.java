package com.example.demo.serde;

import com.example.demo.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class EmployeeDeserializer implements Deserializer<Employee> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Not needed in this case
    }

    @Override
    public Employee deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Employee.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing Employee", e);
        }
    }

    @Override
    public void close() {
        // Not needed in this case
    }
}
