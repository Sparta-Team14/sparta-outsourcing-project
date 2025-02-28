package com.example.jeogiyoproject.domain.menu.entity;

import com.example.jeogiyoproject.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "menu")
public class Menu extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 메뉴 번호

    @Column(length = 100)
    private String name; // 메뉴명
    @Column(length = 100)
    private String Field; // 메뉴소개
    @Column
    private Integer price; // 가격
}
