package com.example.jeogiyoproject.domain.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders_detail")
@NoArgsConstructor
public class OrderDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 주문상세번호

    @Column
    private Integer amount; //수량
}
