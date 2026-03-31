package com.basesetup.login.repository;

import com.basesetup.login.model.Account;
import com.basesetup.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUser(User user);
}
