package com.lazywallet.lazywallet.controllers;

import com.lazywallet.lazywallet.models.UserRegistrationDTO;
import com.lazywallet.lazywallet.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthPageController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserRegistrationDTO registrationDTO, Model model) {
        userService.createUser(registrationDTO);
        return "redirect:/login"; // После успешной регистрации перенаправляем на страницу входа
    }
}

