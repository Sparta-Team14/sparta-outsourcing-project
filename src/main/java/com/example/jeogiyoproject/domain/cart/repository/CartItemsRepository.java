package com.example.jeogiyoproject.domain.cart.repository;

import com.example.jeogiyoproject.domain.cart.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

}
