package com.example.jeogiyoproject.domain.cart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cart_items")
@NoArgsConstructor
public class cartitems {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 장바구니 아이템 번호

    @Column
    private Integer amount; // 수량
}
