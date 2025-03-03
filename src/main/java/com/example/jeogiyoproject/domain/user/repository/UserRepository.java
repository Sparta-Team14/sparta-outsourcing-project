package com.example.jeogiyoproject.domain.user.repository;


import com.example.jeogiyoproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
