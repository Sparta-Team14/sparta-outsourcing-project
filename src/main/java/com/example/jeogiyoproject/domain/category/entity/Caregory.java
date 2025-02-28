package com.example.jeogiyoproject.domain.category.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "category")
@NoArgsConstructor
public class Caregory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 카테고리 번호

    @Column
    private String name; // 카테고리 명
}
