package com.lazywallet.lazywallet.services;

import com.lazywallet.lazywallet.exceptions.EmailAlreadyExistsException;
import com.lazywallet.lazywallet.models.User;
import com.lazywallet.lazywallet.models.UserDTO;
import com.lazywallet.lazywallet.models.UserRegistrationDTO;
import com.lazywallet.lazywallet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
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
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Пытаемся загрузить пользователя по имени: " + username);
        User user = userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPasswordHash())
                .roles("USER") // пока одна роль
                .build();
    }

}
