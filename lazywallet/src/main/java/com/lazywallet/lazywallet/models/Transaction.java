//package com.lazywallet.lazywallet.models;
//
//import com.lazywallet.lazywallet.exceptions.InsufficientFundsException;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.*;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.time.LocalDateTime;
//import java.util.Objects;
//
///**
// * Финансовая операция (доход/расход) с полной историей
// */
//@Entity
//@Table(name = "transactions")
//public class Transaction {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "transaction_id")
//    private Long id;
//
//    @Positive(message = "Amount must be positive")
//    @Digits(integer = 15, fraction = 2, message = "Invalid amount format")
//    @Column(nullable = false, precision = 15, scale = 2)
//    private BigDecimal amount;
//
//    @PastOrPresent(message = "Date cannot be in the future")
//    @Column(nullable = false)
//    private LocalDateTime date;
//
//    @Size(max = 500, message = "Description too long")
//    @Column(length = 500)
//    private String description;
//
//    @NotNull(message = "Category is required")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id", nullable = false)
//    private Category category;
//
//    @NotNull(message = "Account is required")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "account_id", nullable = false)
//    private Account account;
//
//    @NotNull(message = "User is required")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    // ========== Конструкторы ==========
//    public Transaction() {
//        this.date = LocalDateTime.now();
//    }
//
//    public Transaction(BigDecimal amount, Category category, Account account, User user) {
//        this();
//        this.amount = amount;
//        this.category = category;
//        this.account = account;
//        this.user = user;
//    }
//
//    // ========== Бизнес-методы ==========
//    /**
//     * Проверяет, является ли транзакция доходом
//     */
//    public boolean isIncome() {
//        return category.getType() == CategoryType.INCOME;
//    }
//
//    /**
//     * Проверяет, является ли транзакция расходом
//     */
//    public boolean isExpense() {
//        return !isIncome();
//    }
//
//    /**
//     * Применяет транзакцию к связанному счету
//     * @throws InsufficientFundsException если недостаточно средств для расходной операции
//     */
//    @PrePersist
//    @PreUpdate
//    public void applyToAccount() {
//        if (isIncome()) {
//            account.deposit(amount);
//        } else {
//            account.withdraw(amount);
//        }
//    }
//    /**
//     * Устанавливает связь с банковским счетом
//     * @param account Счет, к которому относится транзакция
//     * @throws IllegalArgumentException если account == null
//     */
//    public void setAccount(Account account) {
//        if (account == null) {
//            throw new IllegalArgumentException("Account cannot be null");
//        }
//
//        // Удаляем транзакцию из старого счета (если был)
//        if (this.account != null) {
//            this.account.getTransactions().remove(this);
//        }
//
//        // Устанавливаем новую связь
//        this.account = account;
//        account.getTransactions().add(this);
//    }
//
//    public Account getAccount() {
//        return account;
//    }
//    // ========== Геттеры ==========
//    public Long getId() {
//        return id;
//    }
//
//    public BigDecimal getAmount() {
//        return amount;
//    }
//
//    public LocalDateTime getDate() {
//        return date;
//    }
//
//    // ========== Сеттеры с валидацией ==========
//    public void setAmount(BigDecimal amount) {
//        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
//            throw new IllegalArgumentException("Amount must be positive");
//        }
//        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
//    }
//
//    public void setCategory(Category category) {
//        this.category = Objects.requireNonNull(category, "Category cannot be null");
//    }
//
//    // ========== equals/hashCode/toString ==========
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Transaction)) return false;
//        Transaction that = (Transaction) o;
//        return Objects.equals(id, that.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
//
//    @Override
//    public String toString() {
//        return String.format(
//                "Transaction[id=%d, amount=%.2f, date=%s, category=%s]",
//                id, amount, date, category.getName()
//        );
//    }
//}