package com.lazywallet.lazywallet.services;

import com.lazywallet.lazywallet.exceptions.EmailAlreadyExistsException;
import com.lazywallet.lazywallet.models.User;
import com.lazywallet.lazywallet.models.UserDTO;
import com.lazywallet.lazywallet.models.UserRegistrationDTO;
//import com.lazywallet.lazywallet.models.UserRole;
import com.lazywallet.lazywallet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    public UserDTO createUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByUserNameIgnoreCase(registrationDTO.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        User user = new User();
        user.setEmail(registrationDTO.getEmail());
        user.setPasswordHash(registrationDTO.getPassword());
        user.setUserName(registrationDTO.getUserName());
//        user.setRole(UserRole.USER);

        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }
    public boolean authenticate(String userName, String password) {
        Optional<User> optionalUser = userRepository.findByUserNameIgnoreCase(userName);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return password.equals(user.getPasswordHash());
        }
        return false;  // Если пользователь не найден или пароли не совпадают
    }
    private UserDTO mapToDTO(User user) {
        // Маппинг entity в DTO
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setEmail(user.getEmail());
//        userDTO.setRole(user.getRole());
//        userDTO.setCreatedAt(user.getCreatedAt());

        return userDTO;
    }
}