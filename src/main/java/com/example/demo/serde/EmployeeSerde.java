package com.example.demo.serde;

import com.example.demo.model.Employee;
import org.apache.kafka.common.serialization.Serdes;

public class EmployeeSerde extends Serdes.WrapperSerde<Employee> {
    public EmployeeSerde() {
        super(new EmployeeSerializer(), new EmployeeDeserializer());
    }
}
