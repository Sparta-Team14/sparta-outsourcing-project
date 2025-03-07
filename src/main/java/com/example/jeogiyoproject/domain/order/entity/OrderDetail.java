package com.example.jeogiyoproject.domain.order.entity;

import com.example.jeogiyoproject.domain.menu.entity.Menu;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column
    private Integer quantity; //수량

    public OrderDetail(Order order, Menu menu, Integer quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }
}

