package com.example.jeogiyoproject.domain.cart.repository;

import com.example.jeogiyoproject.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @EntityGraph(attributePaths = {"foodstore", "user"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Cart> findByUserId(Long id);

    List<Cart> findByUpdatedAtBefore(LocalDateTime cutOffTime);
}
