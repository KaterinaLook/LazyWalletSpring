package com.lazywallet.lazywallet.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Финансовая операция (доход/расход) с полной историей
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Getter
    @Positive(message = "Amount must be positive")
    @Digits(integer = 15, fraction = 2, message = "Invalid amount format")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Getter
    @PastOrPresent(message = "Date cannot be in the future")
    @Column(nullable = false)
    private final LocalDateTime DATE;

    @Getter
    @NotNull(message = "Category is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Setter
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Transaction() {
        this.DATE = LocalDateTime.now();
    }

    public Transaction(BigDecimal amount, Category category, User user) {
        this();
        setAmount(amount);
        setCategory(category);
        this.user = user;
    }

    // ========== Сеттеры с валидацией ==========
    public void setAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public void setCategory(Category category) {
        this.category = Objects.requireNonNull(category, "Category cannot be null");
    }

    // ========== equals/hashCode/toString ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
                "Transaction[id=%d, amount=%.2f, DATE=%s, category=%s]",
                id, amount, DATE  //, category.getName()
        );
    }
    /**
     *  Проверяет, является ли транзакция расходом
     */
//        public boolean isExpense() {
//            return !isIncome();
//        }
    /**
     *  Проверяет, является ли транзакция доходом
     */
    //    public boolean isIncome() {
    //        return category.getType() == CategoryType.INCOME;
    //    }
}