package com.basesetup.login.service;

import com.basesetup.login.dto.TransactionEvent;
import com.basesetup.login.model.Transaction;
import com.basesetup.login.model.User;
import com.basesetup.login.repository.TransactionRepository;
import com.basesetup.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @KafkaListener(topics = "transactions", groupId = "bank-group")
    @Transactional
    public void consume(TransactionEvent event) {


        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction t = new Transaction();
        t.setUser(user);
        t.setType(event.getType());
        t.setAmount(event.getAmount());
        t.setDate(new Date());

        transactionRepository.save(t);

    }
}
