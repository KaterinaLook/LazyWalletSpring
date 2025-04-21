//package com.lazywallet.lazywallet.models;
//
//import com.lazywallet.lazywallet.models.CategoryType;
//import jakarta.validation.constraints.*;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
///**
// * Data Transfer Object для финансовых транзакций
// */
//public class TransactionDTO {
//
//    private UUID id;
//
//    @Positive(message = "Сумма должна быть положительной")
//    @Digits(integer = 15, fraction = 2, message = "Некорректный формат суммы")
//    private BigDecimal amount;
//
//    @PastOrPresent(message = "Дата не может быть будущей")
//    private LocalDateTime date;
//
//    @Size(max = 500, message = "Описание слишком длинное")
//    private String description;
//
//    @NotNull(message = "Категория обязательна")
//    private Long categoryId;
//
//    @NotNull(message = "Счет обязателен")
//    private Long accountId;
//
//    private CategoryType categoryType; // Автоматически определяется
//
//    // ======== Конструкторы ========
//    public TransactionDTO() {
//        this.date = LocalDateTime.now();
//    }
//
//    public TransactionDTO(BigDecimal amount, Long categoryId, Long accountId) {
//        this();
//        this.amount = amount;
//        this.categoryId = categoryId;
//        this.accountId = accountId;
//    }
//
//    // ======== Геттеры и сеттеры ========
//    public UUID getId() {
//        return id;
//    }
//
//    public void setId(UUID id) {
//        this.id = id;
//    }
//
//    public BigDecimal getAmount() {
//        return amount;
//    }
//
//    public void setAmount(BigDecimal amount) {
//        this.amount = amount;
//    }
//
//    public LocalDateTime getDATE() {
//        return date;
//    }
//
//    public void setDate(LocalDateTime date) {
//        this.date = date;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public Long getCategoryId() {
//        return categoryId;
//    }
//
//    public void setCategoryId(Long categoryId) {
//        this.categoryId = categoryId;
//    }
//
//    public Long getAccountId() {
//        return accountId;
//    }
//
//    public void setAccountId(Long accountId) {
//        this.accountId = accountId;
//    }
//
//    public CategoryType getCategoryType() {
//        return categoryType;
//    }
//
//    public void setCategoryType(CategoryType categoryType) {
//        this.categoryType = categoryType;
//    }
//
//    // ======== Дополнительные методы ========
//    @Override
//    public String toString() {
//        return "TransactionDTO{" +
//                "id=" + id +
//                ", amount=" + amount +
//                ", date=" + date +
//                ", description='" + description + '\'' +
//                ", categoryId=" + categoryId +
//                ", accountId=" + accountId +
//                '}';
//    }
//}