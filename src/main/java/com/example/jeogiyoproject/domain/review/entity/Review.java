package com.example.jeogiyoproject.domain.review.entity;

import com.example.jeogiyoproject.domain.base.BaseEntity;
import com.example.jeogiyoproject.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 리뷰번호

    @Column(nullable = false)
    private Integer rating; // 별점

    @Column(columnDefinition = "TEXT")
    private String contents; // 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id", nullable = false)  // 주문 기반.
    private Order order;

    public Review(Integer rating, String contents) {
        this.rating = rating;
        this.contents = contents;
    }


    public Review(int rating, String contents, Order order) {
        this.rating = rating;
        this.contents = contents;
        this.order = order;
    }

}

