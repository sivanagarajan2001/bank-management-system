package com.basesetup.login.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;


import java.util.Date;
@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Transaction type is required")
    @Pattern(regexp = "DEPOSIT|WITHDRAW|TRANSFER", message = "Invalid transaction type")
    private String type;
    @Positive(message = "Amount must be greater than 0")
    private double amount;

    // ✅ Many Transactions → One User
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @NotNull(message = "User is required")
    private User user;

    private String createdBy;
    private Date date;
}
