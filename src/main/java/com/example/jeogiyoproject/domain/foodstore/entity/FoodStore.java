package com.example.jeogiyoproject.domain.foodstore.entity;

import com.example.jeogiyoproject.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "foodstores")
@NoArgsConstructor
public class FoodStore extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 가게번호

    @Column(length = 100, nullable = false)
    private String title; // 가게명
    @Column(columnDefinition = "TEXT", nullable = false)
    private String address; // 주소
    @Column
    private Integer minPrice; // 최소주문금액
    @Column
    private LocalDateTime openAt; // 오픈시간
    @Column
    private LocalDateTime closeAt; // 마감시간

    @Column
    private Long userId;

    public FoodStore(Long id, String title, String address, Integer minPrice, LocalDateTime openAt, LocalDateTime closeAt, Long userId) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.minPrice = minPrice;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.userId = userId;
    }
}
