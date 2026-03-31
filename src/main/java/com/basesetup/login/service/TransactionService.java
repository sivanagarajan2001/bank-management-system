package com.basesetup.login.service;

import com.basesetup.login.dto.TransactionProjection;
import com.basesetup.login.dto.TransactionResponse;
import com.basesetup.login.dto.UserResponse;
import com.basesetup.login.model.Transaction;
import com.basesetup.login.model.User;
import com.basesetup.login.repository.TransactionRepository;
import com.basesetup.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public List<TransactionResponse> getUserTransactions(Long userId) {

        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        return transactions.stream()
                .map(this::convertToDto)
                .toList();
    }

    public double getHighestTransaction(Long userId) {

        User user = userRepository.findById(userId).orElseThrow();

        return transactionRepository.findByUser(user)
                .stream()
                .map(Transaction::getAmount)
                .max(Double::compare)
                .orElse(0.0);
    }

    public Page<TransactionResponse> getTransactionsWithFilters(Long userId, String type, Date startDate, Date endDate, Double minAmount, Double maxAmount, int page, int size, String sortBy, String direction) {

        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("asc") ?
                        Sort.by(sortBy).ascending() :
                        Sort.by(sortBy).descending()
        );

        Page<TransactionProjection> pageResult =
                transactionRepository.findTransactionsWithFilters(
                        userId, type, startDate, endDate,
                        minAmount, maxAmount, pageable
                );

        return pageResult.map(p -> {
            TransactionResponse response = new TransactionResponse();

            response.setTransactionId(p.getId());
            response.setType(p.getType());
            response.setAmount(p.getAmount());
            response.setDate(p.getDate());

            UserResponse user = new UserResponse();
            user.setEmail(p.getUserEmail());
            user.setFirstName(p.getUserFirstName());
            user.setLastName(p.getUserLastName());
            user.setRole(p.getUserRole());

            response.setUser(user);

            return response;
        });
    }
    private TransactionResponse convertToDto(Transaction transaction) {

        TransactionResponse response = new TransactionResponse();

        response.setTransactionId(transaction.getId());
        response.setType(transaction.getType());
        response.setAmount(transaction.getAmount());
        response.setDate(transaction.getDate());
        response.setCreatedBy(transaction.getCreatedBy());

        // ✅ Map User → UserResponse
        User user = transaction.getUser();

        if (user != null) {
            UserResponse userResponse = new UserResponse();
            userResponse.setEmail(user.getEmail());
            userResponse.setFirstName(user.getFirstName());
            userResponse.setLastName(user.getLastName());
            userResponse.setRole(user.getRole().name());

            response.setUser(userResponse);
        }

        return response;
    }

}
