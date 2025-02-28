package com.example.jeogiyoproject.domain.review.entity;

import com.example.jeogiyoproject.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 리뷰번호

    @Column
    private Integer rating; // 별점
    @Column(columnDefinition = "TEXT")
    private String contents; // 내용
}
