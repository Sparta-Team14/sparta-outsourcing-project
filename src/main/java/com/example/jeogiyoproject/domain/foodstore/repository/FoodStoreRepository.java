package com.example.jeogiyoproject.domain.foodstore.repository;

import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodStoreRepository extends JpaRepository<FoodStore, Long> {
    List<FoodStore> findByTitleContaining(String title);

    Optional<FoodStore> findById(Long foodStoreId);
    boolean existsByTitleAndAddress(String title, String address);
    long countByUser(User user);
}
