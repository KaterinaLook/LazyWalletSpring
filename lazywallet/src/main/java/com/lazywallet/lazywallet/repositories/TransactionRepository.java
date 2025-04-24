package com.lazywallet.lazywallet.repositories;

import com.lazywallet.lazywallet.models.Transaction;
import com.lazywallet.lazywallet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
//    List<Transaction> findByUserAndType(User user, String type);
}

