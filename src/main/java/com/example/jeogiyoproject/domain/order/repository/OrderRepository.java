package com.example.jeogiyoproject.domain.order.repository;

import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"foodstore, user"})
    @Query("""
            SELECT o
            FROM Order o
            LEFT JOIN o.foodstore f
            LEFT JOIN o.user u
            WHERE f.id = :foodstoreId
              AND (:status IS NULL OR o.status IN :status)
              AND (:startAt IS NULL OR o.createdAt >= :startAt)
              AND (:endAt IS NULL OR o.createdAt < :endAt)
            ORDER BY o.createdAt DESC
            """)
    Page<Order> findAllByFoodstoreIdByCreatedAtDesc(Pageable pageable,
                                                    @Param("foodstoreId") Long foodstoreId,
                                                    @Param("status") List<Status> status,
                                                    @Param("startAt") LocalDateTime startAt,
                                                    @Param("endAt") LocalDateTime endAt);

    @EntityGraph(attributePaths = {"foodstore"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("""
            SELECT o
            FROM Order o
            LEFT JOIN o.foodstore f
            LEFT JOIN o.user u
            WHERE u.id = :userId
              AND (:foodstoreTitle IS NULL OR f.title = :foodstoreTitle)
              AND (:status IS NULL OR o.status IN :status)
              AND (:startAt IS NULL OR o.createdAt >= :startAt)
              AND (:endAt IS NULL OR o.createdAt < :endAt)
            ORDER BY o.createdAt DESC
            """)
    Page<Order> findAllByUserId(Pageable pageable,
                                @Param("userId") Long id,
                                @Param("foodstoreTitle") String foodstoreTitle,
                                @Param("status") List<Status> status,
                                @Param("startAt") LocalDateTime startAt,
                                @Param("endAt") LocalDateTime endAt);
}
