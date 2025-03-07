package com.example.jeogiyoproject.domain.favorite.controller;

import com.example.jeogiyoproject.domain.favorite.service.FavoriteService;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.global.common.annotation.Auth;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import com.example.jeogiyoproject.web.aop.annotation.AuthCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @AuthCheck(UserRole.USER)
    @PostMapping("/foodstores/{foodstoreId}/favorites")
    public Boolean toggleFavorite(
            @Auth AuthUser authUser,
            @PathVariable Long foodstoreId
    ) {
        return favoriteService.toggleFavorite(authUser.getId(), foodstoreId);
    }
}
