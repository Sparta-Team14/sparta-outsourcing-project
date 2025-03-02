package com.example.jeogiyoproject.domain.menu.repository;

import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory,Long> {
}
