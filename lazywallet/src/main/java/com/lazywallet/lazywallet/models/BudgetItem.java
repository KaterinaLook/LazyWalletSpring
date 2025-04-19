//package com.lazywallet.lazywallet.models;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.*;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.Objects;
//
///**
// * Статья бюджета, связывающая категорию с запланированной суммой в рамках BudgetPlan
// */
//@Entity
//@Table(name = "budget_items",
//        uniqueConstraints = @UniqueConstraint(
//                name = "uk_budget_item_plan_category",
//                columnNames = {"budget_plan_id", "category_id"}
//        ))
//public class BudgetItem {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "budget_item_id")
//    private Long id;
//
//    @Positive(message = "Amount must be positive")
//    @Digits(integer = 15, fraction = 2, message = "Invalid amount format")
//    @Column(nullable = false, precision = 15, scale = 2)
//    private BigDecimal amount;
//
//    @NotNull(message = "Category must be specified")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id", nullable = false)
//    private Category category;
//
//    @NotNull(message = "Budget plan must be specified")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "budget_plan_id", nullable = false)
//    private BudgetPlan budgetPlan;
//
//    // ===== Конструкторы =====
//    public BudgetItem() {}
//
//    public BudgetItem(BigDecimal amount, Category category, BudgetPlan budgetPlan) {
//        setAmount(amount);
//        setCategory(category);
//        setBudgetPlan(budgetPlan);
//    }
//
//    // ===== Бизнес-методы =====
//    /**
//     * Проверяет, относится ли статья к доходам
//     */
//    public boolean isIncome() {
//        return category.isIncome();
//    }
//
//    /**
//     * Проверяет, относится ли статья к расходам
//     */
//    public boolean isExpense() {
//        return !isIncome();
//    }
//
//    // ===== Геттеры =====
//    public Long getId() {
//        return id;
//    }
//
//    public BigDecimal getAmount() {
//        return amount;
//    }
//
//    public Category getCategory() {
//        return category;
//    }
//
//    public BudgetPlan getBudgetPlan() {
//        return budgetPlan;
//    }
//
//    // ===== Сеттеры с валидацией =====
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
//    public void setBudgetPlan(BudgetPlan budgetPlan) {
//        this.budgetPlan = Objects.requireNonNull(budgetPlan, "Budget plan cannot be null");
//        if (!budgetPlan.getItems().contains(this)) {
//            budgetPlan.addItem(this);
//        }
//    }
//
//    // ===== equals/hashCode/toString =====
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof BudgetItem item)) return false;
//        return Objects.equals(id, item.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
//
//    @Override
//    public String toString() {
//        return "BudgetItem{" +
//                "id=" + id +
//                ", amount=" + amount +
//                ", category=" + (category != null ? category.getName() : "null") +
//                '}';
//    }
//}