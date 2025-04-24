package com.lazywallet.lazywallet.services;

import com.lazywallet.lazywallet.exceptions.EmailAlreadyExistsException;
import com.lazywallet.lazywallet.models.*;
import com.lazywallet.lazywallet.repositories.CategoryRepository;
import com.lazywallet.lazywallet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private CategoryRepository categoryRepository;

    public void createUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmailIgnoreCase(registrationDTO.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        User user = new User();
        user.setEmail(registrationDTO.getEmail());
        user.setUserName(registrationDTO.getUserName());
        user.setPasswordHash(passwordEncoder.encode(registrationDTO.getPassword()));

        User savedUser = userRepository.save(user);
        createDefaultCategoriesForUser(savedUser);
    }

    private void createDefaultCategoriesForUser(User user) {
        createCategoryIfNotExists("Food", CategoryType.EXPENSE, user);
        createCategoryIfNotExists("Entertainment", CategoryType.EXPENSE, user);
        createCategoryIfNotExists("Transport", CategoryType.EXPENSE, user);
    }

    private void createCategoryIfNotExists(String name, CategoryType type, User user) {
        boolean exists = categoryRepository.findByNameIgnoreCaseAndUser(name, user).isPresent();
        if (!exists) {
            Category category = new Category(name, type, user);
            categoryRepository.save(category);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPasswordHash())
                .build();
    }
    public boolean authenticate(String userName, String password) {
        return userRepository.findByUserNameIgnoreCase(userName)
                .map(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .orElse(false);
    }
}
