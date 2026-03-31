package com.basesetup.login.controller;

import com.basesetup.login.config.UserContextHolder;
import com.basesetup.login.dto.AccountResponse;
import com.basesetup.login.dto.TransactionResponse;

import com.basesetup.login.service.AccountService;
import com.basesetup.login.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserAccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    @GetMapping("/account")
    public AccountResponse getMyAccount() {
        Long userId = UserContextHolder.getUserContext().getId();
        return accountService.getAccount(userId);
    }

    @GetMapping("/transactions")
    public List<TransactionResponse> getTransactions() {
        Long userId = UserContextHolder.getUserContext().getId();
        return transactionService.getUserTransactions(userId);
    }

    @GetMapping("/highest-transaction")
    public double highestTransaction() {
        Long userId = UserContextHolder.getUserContext().getId();
        return transactionService.getHighestTransaction(userId);
    }



}
