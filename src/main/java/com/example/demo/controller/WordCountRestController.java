package com.example.demo.controller;


import com.example.demo.producer.KafkaProducer;
import com.example.demo.service.KafkaStreamService;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class WordCountRestController {

    private final StreamsBuilderFactoryBean factoryBean;

    private final KafkaProducer kafkaProducer;

    @Autowired
    private final KafkaStreamService kafkaStreamService;

    @GetMapping("/count/{word}")
    public Long getWordCount(@PathVariable String word) {
       return kafkaStreamService.getWordCount(word);
     //   return counts.get(word);
    }

    @PostMapping("/reverse")
    public void addMessage(@RequestParam("word") String message) {
        kafkaProducer.sendMessage(message);
    }

}