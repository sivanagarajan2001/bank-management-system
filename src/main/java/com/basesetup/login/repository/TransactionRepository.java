package com.basesetup.login.repository;

import com.basesetup.login.dto.TransactionProjection;
import com.basesetup.login.model.Transaction;
import com.basesetup.login.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByUser(User user);

    @Query("""
    SELECT 
        t.id as id,
        t.type as type,
        t.amount as amount,
        t.date as date,
        u.email as userEmail,
        u.firstName as userFirstName,
        u.lastName as userLastName,
        u.role as userRole
    FROM Transaction t
    JOIN t.user u
    WHERE u.id = :userId
    AND (:type IS NULL OR t.type = :type)
    AND (:startDate IS NULL OR t.date >= :startDate)
    AND (:endDate IS NULL OR t.date <= :endDate)
    AND (:minAmount IS NULL OR t.amount >= :minAmount)
    AND (:maxAmount IS NULL OR t.amount <= :maxAmount)
""")
    Page<TransactionProjection> findTransactionsWithFilters(
            Long userId,
            String type,
            Date startDate,
            Date endDate,
            Double minAmount,
            Double maxAmount,
            Pageable pageable
    );

    List<Transaction> findByUserId(Long userId);
}
