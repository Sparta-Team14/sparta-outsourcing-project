package com.example.jeogiyoproject.domain.favorite.service;

import com.example.jeogiyoproject.domain.favorite.entity.Favorite;
import com.example.jeogiyoproject.domain.favorite.repository.FavoriteRepository;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private FoodStoreRepository foodStoreRepository;
    @Autowired
    private UserRepository userRepository;

    public Boolean toggleFavorite(Long userId, Long foodstoreId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_IS_NOT_EXIST));

        FoodStore foodStore = foodStoreRepository.findById(foodstoreId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOODSTORE_NOT_FOUND));

        Favorite favorite = favoriteRepository.findByUserAndFoodstore(user, foodStore)
                .orElse(new Favorite(user, foodStore));

        favorite.setFavorite(!favorite.getFavorite());
        favoriteRepository.save(favorite);

        return favorite.getFavorite();
    }
}
