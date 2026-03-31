# 🏦 Bank Management System (Spring Boot)

## 📌 Project Description
This is a **Bank Management System API** built using Spring Boot.  
It supports role-based access with JWT authentication and includes Kafka-based transaction processing.

---

## 🚀 Features

### 🔐 Authentication & Authorization
- JWT-based authentication
- Role-based access:
  - **MANAGER**
  - **EMPLOYEE**
  - **USER**

---

### 👨‍💼 Manager APIs
- Get all users
- Manage users (CRUD)

---

### 👨‍🔧 Employee APIs
- Create account
- Deposit money
- Withdraw money
- Transfer money
- View transactions with filters & pagination

---

### 👤 User APIs
- View own account
- View transaction history
- Get highest transaction

---

### 💰 Account Features
- One user → One account
- Balance management
- Transaction tracking

---

### 📊 Transaction Features
- Kafka-based transaction processing
- Filters:
  - Type (DEPOSIT, WITHDRAW, TRANSFER)
  - Date range
  - Amount range
- Pagination & sorting

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- MySQL
- Redis (Session validation)
- Apache Kafka
- Swagger (API docs)

---


### ▶️ Steps to Run the Application

🔴 Start Redis

.\redis-server.exe

---
## 🟡 KAFKA
🟡 Start Kafka

1. Start Zookeeper

zookeeper-server-start.bat ..\..\config\zookeeper.properties

2. Start Kafka Server

kafka-server-start.bat ..\..\config\server.properties

---
## 🔗 API Documentation

Swagger UI is available at:

http://localhost:8081/swagger-ui.html

---

## ⚙️ Configuration

Update in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bank1
spring.datasource.username=root
spring.datasource.password=root

spring.kafka.bootstrap-servers=localhost:9092
spring.redis.host=localhost
spring.redis.port=6379

---
