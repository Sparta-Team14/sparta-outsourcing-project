package com.example.jeogiyoproject.domain.favorite.repository;

import com.example.jeogiyoproject.domain.favorite.entity.Favorite;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface FavoriteRepository extends JpaRepository<Favorite, Long>{
    Optional<Favorite> findByUserAndFoodstore(User user, FoodStore foodStore);
}
