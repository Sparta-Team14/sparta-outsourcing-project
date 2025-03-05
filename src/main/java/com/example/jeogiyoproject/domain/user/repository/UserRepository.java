package com.example.jeogiyoproject.domain.user.repository;


import com.example.jeogiyoproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    //boolean existsByEmail(String email);

//    @Query(value = "SELECT COUNT(email) FROM users WHERE email = ?", nativeQuery = true)
    boolean existsByEmail(String email);
}
