package com.example.jeogiyoproject.domain.foodstore.entity;

import com.example.jeogiyoproject.domain.base.BaseEntity;

import com.example.jeogiyoproject.domain.user.entity.Users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    private Users user;

//    @OneToMany(mappedBy = "foodStore", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Menu> menus = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer minPrice;

    @Column(nullable = false)
    private LocalTime openAt;

    @Column(nullable = false)
    private LocalTime closeAt;

    @Column
    private LocalDateTime deletedAt;

    public FoodStore(
            Users user,
            String title,
            String address,
            Integer minPrice,
            LocalTime openAt,
            LocalTime closeAt
    ) {
        this.user = user;
        this.title = title;
        this.address = address;
        this.minPrice = minPrice;
        this.openAt = openAt;
        this.closeAt = closeAt;
    }

    public void update(
            String title,
            String address,
            Integer minPrice,
            LocalTime openAt,
            LocalTime closeAt
    ) {
        if (title != null) {
            this.title = title;
        }
        if (address != null) {
            this.address = address;
        }
        if (minPrice != null) {
            this.minPrice = minPrice;
        }
        if (openAt != null) {
            this.openAt = openAt;
        }
        if (closeAt != null) {
            this.closeAt = closeAt;
        }
    }

    public Long getUserId() {
        return user.getId();
    }
}
