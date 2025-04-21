package com.lazywallet.lazywallet.controllers;

import com.lazywallet.lazywallet.models.Category;
import com.lazywallet.lazywallet.models.CategoryType;
import com.lazywallet.lazywallet.models.Transaction;
import com.lazywallet.lazywallet.models.User;
import com.lazywallet.lazywallet.repositories.CategoryRepository;
import com.lazywallet.lazywallet.repositories.TransactionRepository;
import com.lazywallet.lazywallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/add-transaction")
    public String addTransaction(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("category") String categoryName,
            @RequestParam("type") String type,
            Model model
    ) {
        // Получаем имя пользователя из авторизации
        String userName = userDetails.getUsername();
        User user = userRepository.findByUserNameIgnoreCase(userName).orElse(null);

        if (user == null) {
            model.addAttribute("error", "Пользователь не найден");
            return "index";
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setUser(user);

        Category category = categoryRepository
                .findByNameIgnoreCaseAndUser(categoryName, user)
                .orElseGet(() -> categoryRepository
                        .findByNameIgnoreCaseAndUser(categoryName, null)
                        .orElseThrow(() -> new RuntimeException("Категория не найдена")));

        transaction.setCategory(category);

        transactionRepository.save(transaction);

        return "redirect:/"; // обновить страницу
    }
    @GetMapping("/api/transactions/stats")
    public List<Map<String, Object>> getExpenseStats(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUserNameIgnoreCase(userDetails.getUsername()).orElse(null);
        if (user == null) return Collections.emptyList();

        List<Transaction> transactions = transactionRepository.findByUser(user);

        // Группируем по категориям и суммируем
        Map<String, BigDecimal> totals = new HashMap<>();
        for (Transaction tx : transactions) {
            if (tx.getCategory().getType() == CategoryType.EXPENSE) {
                String name = tx.getCategory().getName();
                totals.put(name, totals.getOrDefault(name, BigDecimal.ZERO).add(tx.getAmount()));
            }
        }

        // Формируем JSON
        return totals.entrySet().stream()
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("category", e.getKey());
                    map.put("total", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }

}
