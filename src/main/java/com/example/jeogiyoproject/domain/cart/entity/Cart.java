package com.example.jeogiyoproject.domain.cart.entity;

import com.example.jeogiyoproject.domain.base.BaseEntity;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cart")
@NoArgsConstructor
public class Cart extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 장바구니 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foodstore_id")
    private FoodStore foodstore;

    public Cart(User user, FoodStore foodstore) {
        this.user = user;
        this.foodstore = foodstore;
    }

}
