package com.example.jeogiyoproject.domain.menu.repository;

import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryListResponseDto;
import com.example.jeogiyoproject.domain.menu.entity.Menu;
import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu,Long> {
    @Query("SELECT new com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryListResponseDto(c.id, c.name) " +
            "FROM MenuCategory c LEFT JOIN Menu m ON m.menuCategory.id = c.id " +
            "WHERE c.foodStore.id = :foodStoreId AND c.deletedAt IS NULL AND m.deletedAt IS NULL")
    List<MenuCategoryListResponseDto> findCategoriesAndMenusByFoodStoreId(@Param("foodStoreId") Long foodStoreId);

    List<Menu> findMenusByMenuCategoryIdAndDeletedAtIsNull(Long categoryId);

}