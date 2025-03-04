package com.example.jeogiyoproject.domain.user.repository;


import com.example.jeogiyoproject.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    //boolean existsByEmail(String email);

    @Query(value = "SELECT COUNT(email) FROM users WHERE email = ?", nativeQuery = true)
    Long existsByEmail(String email);
}
