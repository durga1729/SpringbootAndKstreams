package com.example.demo.processor;

import java.util.Arrays;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WordCountProcessor {

    private static final Serde<String> STRING_SERDE = Serdes.String();

  /*  @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, String> messageStream = streamsBuilder
            .stream("input-topic");
        KStream<String, String> reversedStream = messageStream.filter((key, value) ->value.startsWith("p")).mapValues(value -> new StringBuilder(value).reverse().toString());
        reversedStream.to("output-topic");
    }*/
}