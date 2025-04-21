package com.lazywallet.lazywallet.controllers;

import com.lazywallet.lazywallet.models.Category;
import com.lazywallet.lazywallet.models.Transaction;
import com.lazywallet.lazywallet.models.User;
import com.lazywallet.lazywallet.repositories.CategoryRepository;
import com.lazywallet.lazywallet.repositories.TransactionRepository;
import com.lazywallet.lazywallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.math.BigDecimal;

@Controller
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
}
