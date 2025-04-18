package com.lazywallet.lazywallet.repository;

import com.lazywallet.lazywallet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с пользователями
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Базовые методы наследуются от JpaRepository:
    // save(), findById(), findAll(), deleteById() и др.

    /**
     * Находит пользователя по email (регистронезависимый поиск)
     * @param email Email пользователя
     * @return Optional с пользователем
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Проверяет существование пользователя с email
     * @param email Email для проверки
     * @return true если пользователь существует
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Проверяет существование пользователя с username
     * @param username Имя пользователя для проверки
     * @return true если имя занято
     */
    boolean existsByUsernameIgnoreCase(String username);

    /**
     * Находит пользователя с подгруженными счетами
     * @param userId ID пользователя
     * @return Optional с пользователем и его счетами
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.accounts WHERE u.id = :userId")
    Optional<User> findByIdWithAccounts(@Param("userId") UUID userId);

    /**
     * Находит пользователя с подгруженными категориями
     * @param userId ID пользователя
     * @return Optional с пользователем и его категориями
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.categories WHERE u.id = :userId")
    Optional<User> findByIdWithCategories(@Param("userId") UUID userId);

    /**
     * Находит пользователя по ID с полной загрузкой связанных данных
     * @param userId ID пользователя
     * @return Optional с полностью загруженным пользователем
     */
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.accounts " +
            "LEFT JOIN FETCH u.categories " +
            "WHERE u.id = :userId")
    Optional<User> findByIdFull(@Param("userId") UUID userId);
}