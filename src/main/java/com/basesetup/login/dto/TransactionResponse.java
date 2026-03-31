package com.basesetup.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private Long transactionId;

    private String type;

    private double amount;

    private Date date;

    private String createdBy;

    private UserResponse user;
}
