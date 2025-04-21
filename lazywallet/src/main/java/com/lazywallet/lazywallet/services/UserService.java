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

    public UserDTO createUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByUserNameIgnoreCase(registrationDTO.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        System.out.println("Пароль до хеширования: " + registrationDTO.getPassword());
        System.out.println("Hashed password: " + passwordEncoder.encode(registrationDTO.getPassword()));

        User user = new User();
        user.setEmail(registrationDTO.getEmail());
        user.setUserName(registrationDTO.getUserName());
        user.setPasswordHash(passwordEncoder.encode(registrationDTO.getPassword()));

        User savedUser = userRepository.save(user);
        System.out.println("User saved");
        createDefaultCategoriesForUser(savedUser);
        return mapToDTO(savedUser);
    }

    public boolean authenticate(String userName, String password) {
        Optional<User> optionalUser = userRepository.findByUserNameIgnoreCase(userName);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean passwordMatches = passwordEncoder.matches(password, user.getPasswordHash());
            if (!passwordMatches) {
                System.out.println("Password does not match");
            }
            System.out.println("Password match");
            return passwordEncoder.matches(password, user.getPasswordHash()); // сверяем хэш
        }
        return false;
    }

    private UserDTO mapToDTO(User user) {
        System.out.println("Trying mapToDo()");
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Trying loadUserByUsername(): " + username);
        User user = userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
        System.out.println("User was successfully load");
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPasswordHash())
                .roles("USER") // пока одна роль
                .build();

    }

}
