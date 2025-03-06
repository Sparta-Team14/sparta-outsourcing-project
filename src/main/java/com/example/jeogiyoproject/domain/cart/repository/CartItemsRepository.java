package com.example.jeogiyoproject.domain.cart.repository;

import com.example.jeogiyoproject.domain.cart.entity.CartItems;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
    @EntityGraph(attributePaths = {"cart", "menu"}, type = EntityGraph.EntityGraphType.FETCH)
    List<CartItems> findByCartId(Long cartId);

    @EntityGraph(attributePaths = {"cart", "menu"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<CartItems> findByCartIdAndMenuId(Long cartId, Long menuId);

    int deleteByCartIdIn(List<Long> cartIds);
}
