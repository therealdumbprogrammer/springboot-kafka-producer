package com.thecodealchemist.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping
    public ResponseEntity<String> order(@RequestBody String order) {
        System.out.println("Input order:: "+ order);

        //trigger an event
        kafkaTemplate.send("orderTopic", "Order_Create " + order)
                .whenComplete((sendResult, throwable) -> {
            if(throwable == null) {
                System.out.println(sendResult);
            } else {
                throwable.printStackTrace();
            }
        });

        return ResponseEntity.ok("Order created successfully");
    }
}
