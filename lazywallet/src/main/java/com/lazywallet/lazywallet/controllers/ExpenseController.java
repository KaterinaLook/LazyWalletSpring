package com.lazywallet.lazywallet.controllers;

import com.lazywallet.lazywallet.models.Expense;
import com.lazywallet.lazywallet.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/")
    public String home(Model model) {
        // Добавляем все расходы в модель для отображения на одной странице
        model.addAttribute("expenses", expenseService.getAllExpenses());
        model.addAttribute("expense", new Expense()); // Пустой объект для формы
        return "index"; // Возвращаем шаблон index.html
    }

    @PostMapping("/add-expense")
    public String addExpense(@ModelAttribute Expense expense, Model model) {
        expenseService.addExpense(expense); // Добавляем новый расход
        return "redirect:/"; // Перенаправляем обратно на главную страницу
    }
}

