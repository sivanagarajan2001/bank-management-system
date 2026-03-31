package com.basesetup.login.service;

import com.basesetup.login.dto.AccountResponse;
import com.basesetup.login.dto.TransactionEvent;
import com.basesetup.login.model.Account;
import com.basesetup.login.model.User;
import com.basesetup.login.repository.AccountRepository;
import com.basesetup.login.repository.TransactionRepository;
import com.basesetup.login.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Date;
@Validated
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final KafkaProducer kafkaProducer;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public AccountResponse createAccount( Long userId, double balance) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedinUser = (User) authentication.getPrincipal();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (accountRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Account already exists for user");
        }
        Account account = new Account();
        account.setUser(user);
        account.setBalance(balance);
        account.setCreatedBy(loggedinUser.getEmail());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        account.setUpdatedBy(loggedinUser.getEmail());

        accountRepository.save(account);

        // 🔥 SEND TO KAFKA
        TransactionEvent event = new TransactionEvent();
        event.setUserId(userId);
        event.setType("DEPOSIT");
        event.setAmount(balance);

        kafkaProducer.send(event);
        return new AccountResponse(userId,account.getBalance(),userService.getUserResponse(user));
    }

    public AccountResponse getAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Account account=accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return new AccountResponse(userId,account.getBalance(),userService.getUserResponse(user));
    }

    @Transactional
    public AccountResponse deposit(Long userId, double amount) {

        User user = userRepository.findById(userId).orElseThrow();
        Account acc = accountRepository.findByUser(user).orElseThrow();

        acc.setBalance(acc.getBalance() + amount);
        accountRepository.save(acc);

        // 🔥 SEND TO KAFKA
        TransactionEvent event = new TransactionEvent();
        event.setUserId(userId);
        event.setType("DEPOSIT");
        event.setAmount(amount);

        kafkaProducer.send(event);

        return new AccountResponse(userId,acc.getBalance(),userService.getUserResponse(user));
    }

    @Transactional
    public AccountResponse withdraw(Long userId, double amount) {

        User user = userRepository.findById(userId).orElseThrow();
        Account acc = accountRepository.findByUser(user).orElseThrow();

        if (acc.getBalance() < amount)
            throw new RuntimeException("Insufficient balance");

        acc.setBalance(acc.getBalance() - amount);
        accountRepository.save(acc);

        TransactionEvent event = new TransactionEvent();
        event.setUserId(userId);
        event.setType("WITHDRAW");
        event.setAmount(amount);

        kafkaProducer.send(event);

        return new AccountResponse(userId,acc.getBalance(),userService.getUserResponse(user));
    }

    @Transactional
    public void transfer(Long fromUserId, Long toUserId, double amount) {
        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }
        User fromUser = userRepository.findById(fromUserId).orElseThrow(() -> new RuntimeException("Sender account not found"));
        User toUser = userRepository.findById(toUserId).orElseThrow(() -> new RuntimeException("To account not found"));

        Account fromAcc = accountRepository.findByUser(fromUser).orElseThrow(() -> new RuntimeException("Sender account not found"));
        Account toAcc = accountRepository.findByUser(toUser).orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (fromAcc.getBalance() < amount)
            throw new RuntimeException("Insufficient balance");

        fromAcc.setBalance(fromAcc.getBalance() - amount);
        toAcc.setBalance(toAcc.getBalance() + amount);

        accountRepository.save(fromAcc);
        accountRepository.save(toAcc);

        // 🔥 Kafka event
        TransactionEvent event = new TransactionEvent();
        event.setUserId(fromUserId);
        event.setType("TRANSFER");
        event.setAmount(amount);

        kafkaProducer.send(event);
    }
}
