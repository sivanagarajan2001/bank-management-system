package com.basesetup.login.service;

import com.basesetup.login.dto.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    private static final String TOPIC = "transactions";

    public void send(TransactionEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
