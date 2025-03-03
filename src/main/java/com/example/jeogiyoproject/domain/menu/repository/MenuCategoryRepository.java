package com.example.jeogiyoproject.domain.menu.repository;

import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory,Long> {
    List<MenuCategory> findAllByFoodStoreIdAndDeletedAtIsNull(Long foodstoreId);
    List<MenuCategory> findAllByFoodStoreIdAndDeletedAtIsNotNull(Long foodstoreId);
}
