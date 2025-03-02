package com.example.jeogiyoproject.domain.cart.entity;

import com.example.jeogiyoproject.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cart_items")
@NoArgsConstructor
public class CartItems {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 장바구니 아이템 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column
    private Integer quantity; // 수량
}
