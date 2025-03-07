package com.example.jeogiyoproject.domain.cart.entity;

import com.example.jeogiyoproject.domain.menu.entity.Menu;
import com.example.jeogiyoproject.domain.order.dto.request.CreateOrderRequestDto;
import com.example.jeogiyoproject.domain.order.dto.request.OrderMenuRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "cart_items")
@NoArgsConstructor
public class CartItems {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 장바구니 아이템 번호

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column
    private Integer quantity; // 수량

    public CartItems(Cart cart, Menu menu, Integer quantity) {
        this.cart = cart;
        this.menu = menu;
        this.quantity = quantity;
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
