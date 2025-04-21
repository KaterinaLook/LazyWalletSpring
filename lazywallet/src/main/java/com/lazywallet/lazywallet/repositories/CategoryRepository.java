package com.lazywallet.lazywallet.repositories;

import com.lazywallet.lazywallet.models.Category;
import com.lazywallet.lazywallet.models.CategoryType;
import com.lazywallet.lazywallet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCaseAndUser(String name, User user);

    List<Category> findByUserOrUserIsNull(User user); // Все категории: кастомные и дефолтные

    List<Category> findByUserOrUserIsNullAndType(User user, CategoryType type); // если хочешь фильтровать по типу
}
