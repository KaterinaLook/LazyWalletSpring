package com.lazywallet.lazywallet.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * План бюджета на определенный период
 */
@Entity
@Table(name = "budget_plans",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_budget_plan_user_period",
                columnNames = {"user_id", "start_date", "end_date"}
        ))
public class BudgetPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_plan_id")
    private Long id;

    @NotBlank(message = "Plan name cannot be blank")
    @Size(max = 100, message = "Plan name too long")
    @Column(nullable = false, length = 100)
    private String name;

    @FutureOrPresent(message = "Start date must be in present or future")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Future(message = "End date must be in future")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull(message = "User must be specified")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "budgetPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetItem> items = new ArrayList<>();

    // ===== Конструкторы =====
    public BudgetPlan() {}

    public BudgetPlan(String name, LocalDate startDate, LocalDate endDate, User user) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
    }

    // ===== Бизнес-методы =====
    /**
     * Добавляет статью бюджета (двунаправленная связь)
     */
    public void addItem(BudgetItem item) {
        items.add(item);
        item.setBudgetPlan(this);
    }

    /**
     * Возвращает общий запланированный доход
     */
    public BigDecimal getTotalPlannedIncome() {
        return items.stream()
                .filter(item -> item.getCategory().isIncome())
                .map(BudgetItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Возвращает общий запланированный расход
     */
    public BigDecimal getTotalPlannedExpense() {
        return items.stream()
                .filter(item -> !item.getCategory().isIncome())
                .map(BudgetItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Проверяет, активен ли план (текущая дата в периоде)
     */
    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    // ===== Геттеры =====
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public User getUser() {
        return user;
    }

    public List<BudgetItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    // ===== Сеттеры с валидацией =====
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Plan name cannot be empty");
        }
        this.name = name.trim();
    }

    public void setStartDate(LocalDate startDate) {
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (startDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        this.endDate = endDate;
    }

    public void setUser(User user) {
        this.user = Objects.requireNonNull(user, "User cannot be null");
    }

    // ===== equals/hashCode/toString =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BudgetPlan that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BudgetPlan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", period=" + startDate + ".." + endDate +
                '}';
    }
}