package com.basesetup.login.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
//import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "User is required")
    private double balance;
    // ✅ One Account → One User
    @OneToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User is required")
    private User user;
    private LocalDateTime createdAt;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
