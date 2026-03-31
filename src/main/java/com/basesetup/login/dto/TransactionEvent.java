package com.basesetup.login.dto;

import lombok.Data;

@Data
public class TransactionEvent {
    private Long userId;
    private String type;
    private double amount;
}
