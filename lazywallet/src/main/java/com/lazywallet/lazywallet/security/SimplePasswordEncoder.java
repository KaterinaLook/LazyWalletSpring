//package com.lazywallet.lazywallet.security;
//
//import org.springframework.stereotype.Component;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Component
//public class SimplePasswordEncoder implements PasswordEncoder {
//
//    @Override
//    public String encode(CharSequence rawPassword) {
//        // Простое "шифрование" — добавление префикса (для примера)
//        return "{encoded}" + rawPassword;
//    }
//
//    @Override
//    public boolean matches(CharSequence rawPassword, String encodedPassword) {
//        // Проверяем, что "зашифрованный" пароль совпадает
//        return encodedPassword.equals(encode(rawPassword));
//    }
//}
//
