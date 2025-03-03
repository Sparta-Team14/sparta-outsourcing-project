package com.example.jeogiyoproject.domain.account.repository;


import com.example.jeogiyoproject.domain.account.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
