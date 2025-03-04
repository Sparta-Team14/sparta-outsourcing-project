package com.example.jeogiyoproject.domain.user.entity;

import com.example.jeogiyoproject.domain.base.BaseEntity;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deletedAt = true WHERE id = ?")
@Where(clause = "deletedAt = 0")
@NoArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 회원번호

    @Column(length = 100, nullable = false)
    private String email; // 이메일
    @Column(length = 100, nullable = false)
    private String password; // 비밀번호
    @Column(length = 15, nullable = false)
    private String name; // 이름
    @Column(columnDefinition = "TEXT", nullable = false)
    private String address; // 주소
    @Column(length = 5, nullable = false)
    private UserRole role; // 역할

    @Column(name = "deletedAt") // default 설정 제외
    private boolean deleted;

    public User(String email, String password, String name, String address, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.role = role;
    }

    public void update(String address) {
        this.address = address;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }
}
