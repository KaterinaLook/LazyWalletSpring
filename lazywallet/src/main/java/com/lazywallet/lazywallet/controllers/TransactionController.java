package com.lazywallet.lazywallet.controllers;

import com.lazywallet.lazywallet.models.Transaction;
import com.lazywallet.lazywallet.models.User;
import com.lazywallet.lazywallet.repositories.TransactionRepository;
import com.lazywallet.lazywallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add-transaction")
    public String addTransaction(
            @RequestParam("user_id") UUID userId,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("type") String type,
            Model model
    ) {
        // Найдём пользователя по email (временно, пока нет авторизации)
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            model.addAttribute("error", "Пользователь не найден");
            return "index";
        }

        // Если это расход, делаем amount отрицательным
        if ("expense".equals(type) && amount.compareTo(BigDecimal.ZERO) > 0) {
            amount = amount.negate();
        }

        // Создаем и сохраняем транзакцию
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setUser(user);
        // transaction.setCategory(...); // когда будешь использовать категории

        transactionRepository.save(transaction);

        return "redirect:/"; // обновить страницу
    }
}
