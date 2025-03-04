package com.example.jeogiyoproject.domain.order.repository;

import com.example.jeogiyoproject.domain.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @EntityGraph(attributePaths = {"order"}, type = EntityGraph.EntityGraphType.FETCH)
    List<OrderDetail> findAllByOrderId(Long orderId);
}
