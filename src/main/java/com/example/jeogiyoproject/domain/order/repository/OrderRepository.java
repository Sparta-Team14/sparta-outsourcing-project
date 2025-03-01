package com.example.jeogiyoproject.domain.order.repository;

import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"foodStore, user"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("""
            SELECT o
            FROM Order o 
            LEFT JOIN o.foodStore f
            LEFT JOIN o.user u
            WHERE (:status IS NULL OR o.status = :status) 
              AND (:startAt IS NULL OR o.createdAt > :startAt)
              AND (:endAt IS NULL OR o.createdAt < :endAt)
            ORDER BY o.createdAt DESC
            """)
    Page<Order> findAllByCreatedAtDesc(Pageable pageable,
                                       @Param("status") Status status,
                                       @Param("startAt") LocalDate startAt,
                                       @Param("endAt") LocalDate endAt);
}
