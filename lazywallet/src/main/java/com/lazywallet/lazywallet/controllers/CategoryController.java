package com.lazywallet.lazywallet.controllers;

import com.lazywallet.lazywallet.models.Category;
import com.lazywallet.lazywallet.models.User;
import com.lazywallet.lazywallet.repositories.CategoryRepository;
import com.lazywallet.lazywallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Category> getCategories(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUserNameIgnoreCase(username).orElse(null);
        return categoryRepository.findByUserOrUserIsNull(user);
    }
}