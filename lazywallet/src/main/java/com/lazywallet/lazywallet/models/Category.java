//package com.lazywallet.lazywallet.models;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.*;
//import java.util.*;
//
///**
// * Категория для классификации транзакций (доходы/расходы)
// */
//@Entity
//@Table(name = "categories",
//        uniqueConstraints = @UniqueConstraint(
//                name = "uk_category_name_user",
//                columnNames = {"name", "user_id"}
//        ))
//public class Category {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "category_id")
//    private Long id;
//
//    @NotBlank(message = "Category name cannot be blank")
//    @Size(min = 2, max = 50, message = "Category name must be between 2-50 characters")
//    @Column(nullable = false, length = 50)
//    private String name;
//
//    @NotNull(message = "Category type must be specified")
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, length = 10)
//    private CategoryType type;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Transaction> transactions = new ArrayList<>();
//
//    @OneToMany(mappedBy = "category")
//    private List<BudgetItem> budgetItems;
//
//    // ===== Конструкторы =====
//    public Category() {}
//
//    public Category(String name, CategoryType type, User user) {
//        this.name = name;
//        this.type = type;
//        this.user = user;
//    }
//
//    // ===== Бизнес-методы =====
//    /**
//     * Добавляет транзакцию в категорию (двунаправленная связь)
//     */
//    public void addTransaction(Transaction transaction) {
//        transactions.add(transaction);
//        transaction.setCategory(this);
//    }
//
//    /**
//     * Проверяет, является ли категория доходом
//     */
//    public boolean isIncome() {
//        return type == CategoryType.INCOME;
//    }
//
//    // ===== Геттеры =====
//    public Long getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public CategoryType getType() {
//        return type;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public List<Transaction> getTransactions() {
//        return Collections.unmodifiableList(transactions);
//    }
//
//    // ===== Сеттеры с валидацией =====
//    public void setName(String name) {
//        if (name == null || name.trim().isEmpty()) {
//            throw new IllegalArgumentException("Category name cannot be empty");
//        }
//        this.name = name.trim();
//    }
//
//    public void setType(CategoryType type) {
//        this.type = Objects.requireNonNull(type, "Category type cannot be null");
//    }
//
//    public void setUser(User user) {
//        this.user = Objects.requireNonNull(user, "User cannot be null");
//    }
//
//    // ===== equals/hashCode/toString =====
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Category category)) return false;
//        return Objects.equals(id, category.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
//
//    @Override
//    public String toString() {
//        return "Category{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", type=" + type +
//                '}';
//    }
//}