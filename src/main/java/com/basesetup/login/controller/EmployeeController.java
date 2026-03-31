package com.basesetup.login.controller;

import com.basesetup.login.dto.AccountResponse;
import com.basesetup.login.dto.TransactionResponse;
import com.basesetup.login.model.Account;
import com.basesetup.login.service.AccountService;
import com.basesetup.login.service.TransactionService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
@Validated
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public AccountResponse deposit(@RequestParam @NotNull(message = "UserId is required") Long userId, @RequestParam @Positive(message = "Deposit amount must be greater than 0") double amount) {
        return accountService.deposit(userId, amount);
    }

    @PostMapping("/withdraw")
    public AccountResponse withdraw(@RequestParam @NotNull(message = "UserId is required") Long userId,  @RequestParam @Positive(message = "Withdraw amount must be greater than 0") double amount) {
        return accountService.withdraw(userId, amount);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam @NotNull(message = "From UserId is required") Long from,
                           @RequestParam @NotNull(message = "To UserId is required") Long to,
                           @RequestParam @Positive(message = "Transfer amount must be greater than 0") double amount) {

        accountService.transfer(from, to, amount);
        return "Transfer successful";

    }
    @PostMapping("/create-account")
    public AccountResponse createAccount(@RequestParam @NotNull(message = "UserId is required") Long userId,
                                 @RequestParam @PositiveOrZero(message = "Initial balance cannot be negative") double initialBalance) {
        return accountService.createAccount(userId, initialBalance);
    }
    @GetMapping("/account/{userId}")
    public AccountResponse getAccount(@PathVariable @NotNull(message = "UserId is required") Long userId) {
        return accountService.getAccount(userId);
    }
    @GetMapping("/user/{userId}/transactions")
    public Page<TransactionResponse> getTransactions(
            @PathVariable @NotNull(message = "UserId is required") Long userId,

            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") @Positive int size,

            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return transactionService.getTransactionsWithFilters(
                userId, type, startDate, endDate,
                minAmount, maxAmount,
                page, size, sortBy, direction
        );
    }
}
