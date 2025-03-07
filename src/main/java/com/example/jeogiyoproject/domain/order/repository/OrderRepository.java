package com.example.jeogiyoproject.domain.order.repository;

import com.example.jeogiyoproject.domain.admin.dto.DailyOrderCountDto;
import com.example.jeogiyoproject.domain.admin.dto.DailyOrderTotalDto;
import com.example.jeogiyoproject.domain.admin.dto.MonthlyOrderCountDto;
import com.example.jeogiyoproject.domain.admin.dto.MonthlyOrderTotalDto;
import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"foodstore", "user"}, type = EntityGraph.EntityGraphType.FETCH)
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

    @EntityGraph(attributePaths = {"foodstore", "user"}, type = EntityGraph.EntityGraphType.FETCH)
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

    @Query("SELECT new com.example.jeogiyoproject.domain.admin.dto.DailyOrderCountDto" +
            "(YEAR(o.createdAt), MONTH(o.createdAt), DAY(o.createdAt), COUNT(o)) " +
            "FROM Order o " +
            "WHERE FUNCTION('YEAR', o.createdAt) = :year " +
            "AND FUNCTION('MONTH', o.createdAt) = :month " +
            "AND o.status = 'ACCEPTED' " +
            "AND o.deletedAt IS NULL " +
            "GROUP BY YEAR(o.createdAt), MONTH(o.createdAt), DAY(o.createdAt)")
    List<DailyOrderCountDto> findDailyOrderCountsByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT new com.example.jeogiyoproject.domain.admin.dto.DailyOrderTotalDto" +
            "(YEAR(o.createdAt), MONTH(o.createdAt), DAY(o.createdAt), SUM(o.totalPrice)) " +
            "FROM Order o " +
            "WHERE FUNCTION('YEAR', o.createdAt) = :year " +
            "AND FUNCTION('MONTH', o.createdAt) = :month " +
            "AND o.deletedAt IS NULL " +
            "AND o.status = 'ACCEPTED' " +
            "GROUP BY YEAR(o.createdAt), MONTH(o.createdAt), DAY(o.createdAt)")
    List<DailyOrderTotalDto> findDailyOrderTotalByYearAndMonth(@Param("year") int year, @Param("month") int month);


    @Query("SELECT new com.example.jeogiyoproject.domain.admin.dto.MonthlyOrderCountDto" +
            "(YEAR(o.createdAt), MONTH(o.createdAt), COUNT(o)) " +
            "FROM Order o " +
            "WHERE FUNCTION('YEAR', o.createdAt) = :year " +
            "AND o.status = 'ACCEPTED' " +
            "AND o.deletedAt IS NULL " +
            "GROUP BY YEAR(o.createdAt), MONTH(o.createdAt)")
    List<MonthlyOrderCountDto> findMonthlyOrderCountByYear(@Param("year") int year);

    @Query("SELECT new com.example.jeogiyoproject.domain.admin.dto.MonthlyOrderTotalDto" +
            "(YEAR(o.createdAt), MONTH(o.createdAt), SUM(o.totalPrice)) " +
            "FROM Order o " +
            "WHERE FUNCTION('YEAR', o.createdAt) = :year " +
            "AND o.status = 'ACCEPTED' " +
            "AND o.deletedAt IS NULL " +
            "GROUP BY YEAR(o.createdAt), MONTH(o.createdAt)")
    List<MonthlyOrderTotalDto> findMonthlyOrderTotalByYear(@Param("year") int year);
}
