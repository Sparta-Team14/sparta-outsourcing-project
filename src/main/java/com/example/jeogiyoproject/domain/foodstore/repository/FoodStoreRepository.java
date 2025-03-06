package com.example.jeogiyoproject.domain.foodstore.repository;

import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FoodStoreRepository extends JpaRepository<FoodStore, Long> {
    List<FoodStore> findByTitleContaining(String title);

    Optional<FoodStore> findById(Long foodStoreId);
    boolean existsByTitleAndAddress(String title, String address);
    long countByUser(User user);

    @Query("SELECT COUNT(fs) FROM FoodStore fs WHERE fs.user = :user AND fs.deletedAt IS NULL")
    long countActiveStoresByUser(@Param("user") User user);
}
