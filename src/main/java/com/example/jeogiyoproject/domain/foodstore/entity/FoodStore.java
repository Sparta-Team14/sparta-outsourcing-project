package com.example.jeogiyoproject.domain.foodstore.entity;

import com.example.jeogiyoproject.domain.account.entity.Account;
import com.example.jeogiyoproject.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "foodstores")
public class FoodStore extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Account account;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer minPrice;

    @Column(nullable = false)
    private LocalDateTime openAt;

    @Column(nullable = false)
    private LocalDateTime closeAt;

    @Column
    private LocalDateTime deletedAt;

    public FoodStore(
            Account account,
            String title,
            String address,
            Integer minPrice,
            LocalDateTime openAt,
            LocalDateTime closeAt
    ) {
        this.account = account;
        this.title = title;
        this.address = address;
        this.minPrice = minPrice;
        this.openAt = openAt;
        this.closeAt = closeAt;
    }
}
