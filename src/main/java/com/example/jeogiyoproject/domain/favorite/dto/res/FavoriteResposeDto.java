package com.example.jeogiyoproject.domain.favorite.dto.res;

import lombok.Getter;

@Getter
public class FavoriteResposeDto{
    private final Long id;
    private final Long userId;
    private final Long foodStoreId;
    private final Boolean favorite;

    public FavoriteResposeDto(Long id, Long userId, Long foodStoreId, Boolean favorite) {
        this.id = id;
        this.userId = userId;
        this.foodStoreId = foodStoreId;
        this.favorite = favorite;
    }
}
