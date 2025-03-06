package com.example.jeogiyoproject.domain.order.entity;

import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.base.BaseEntity;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.order.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
@SQLDelete(sql = "UPDATE orders SET deleted_at = now() WHERE id = ?")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foodstore_id")
    private FoodStore foodstore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status = Status.WAIT;

    private Integer totalPrice;

    private Integer totalQuantity;

    private String request;

    private LocalDateTime deletedAt;

    public Order(FoodStore foodstore, User user, Integer totalPrice, Integer totalQuantity, String request) {
        this.foodstore = foodstore;
        this.user = user;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.request = request;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}
