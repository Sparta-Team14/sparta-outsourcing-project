package com.example.jeogiyoproject.domain.account.entity;

import com.example.jeogiyoproject.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class Account extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 회원번호

    @Column(length = 100, nullable = false)
    private String email; // 이메일
    @Column(length = 100, nullable = false)
    private String password; // 비밀번호
    @Column(length = 15, nullable = false)
    private String userName; // 이름
    @Column(columnDefinition = "TEXT", nullable = false)
    private String address; // 주소
    @Column(length = 5, nullable = false)
    private String role; // 역할

}
