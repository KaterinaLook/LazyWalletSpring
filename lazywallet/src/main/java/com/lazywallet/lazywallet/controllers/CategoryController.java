package com.lazywallet.lazywallet.controllers;

import com.lazywallet.lazywallet.models.Category;
import com.lazywallet.lazywallet.models.CategoryType;
import com.lazywallet.lazywallet.models.User;
import com.lazywallet.lazywallet.repositories.CategoryRepository;
import com.lazywallet.lazywallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody Map<String, String> body,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        String name = body.get("name");
        String typeStr = body.get("type");

        if (name == null || typeStr == null) {
            return ResponseEntity.badRequest().body("Name and type are required");
        }

        CategoryType type;
        try {
            type = CategoryType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid category type");
        }

        User user = userRepository.findByUserNameIgnoreCase(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (categoryRepository.existsByNameIgnoreCaseAndUser(name, user)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category already exists");
        }

        Category category = new Category(name, type, user);
        categoryRepository.save(category);
        return ResponseEntity.ok("Category added");
    }

}