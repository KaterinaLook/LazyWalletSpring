//package com.lazywallet.lazywallet.repository;
//
//import com.lazywallet.lazywallet.models.Account;
//import org.hibernate.validator.constraints.UUID;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface AccountRepository extends JpaRepository<Account, UUID> {
//    List<Account> findByUserId(UUID userId);
//}