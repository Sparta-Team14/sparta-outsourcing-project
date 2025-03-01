package com.example.jeogiyoproject.domain.order.entity;

import com.example.jeogiyoproject.domain.account.entity.Account;
import com.example.jeogiyoproject.domain.base.BaseEntity;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.order.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
@SQLDelete(sql = "UPDATE orders SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foodstore_id")
    private FoodStore foodStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Account user; // Account?

    @Enumerated(EnumType.STRING)
    private Status status = Status.WAIT;

    private Integer totalPrice;

    private String request;

    private LocalDateTime deletedAt;

    public Order(FoodStore foodStore, Account user, Integer totalPrice, String request) {
        this.foodStore = foodStore;
        this.user = user;
        this.totalPrice = totalPrice;
        this.request = request;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}
