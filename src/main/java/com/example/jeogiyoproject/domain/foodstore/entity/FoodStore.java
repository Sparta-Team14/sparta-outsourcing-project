package com.example.jeogiyoproject.domain.foodstore.entity;

import com.example.jeogiyoproject.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Integer min_price; // 최소주문금액
    @Column
    private LocalDateTime open_at; // 오픈시간
    @Column
    private LocalDateTime close_at; // 마감시간

}
