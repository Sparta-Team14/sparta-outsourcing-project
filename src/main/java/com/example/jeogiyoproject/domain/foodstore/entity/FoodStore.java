package com.example.jeogiyoproject.domain.foodstore.entity;
import com.example.jeogiyoproject.domain.base.BaseEntity;
import com.example.jeogiyoproject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "foodstores")
public class FoodStore extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer minPrice;

    @Column(nullable = false)
    private LocalTime openAt;

    @Column(nullable = false)
    private LocalTime closeAt;

    @Column
    private String imageUrl;

    @Column
    private LocalDateTime deletedAt;

    public FoodStore(
            User user,
            String title,
            String address,
            Integer minPrice,
            LocalTime openAt,
            LocalTime closeAt,
            String imgUrl
    ) {
        this.user = user;
        this.title = title;
        this.address = address;
        this.minPrice = minPrice;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.imageUrl = imgUrl;
    }

    public void update(
            String title,
            String address,
            Integer minPrice,
            LocalTime openAt,
            LocalTime closeAt,
            MultipartFile image) {
        if (title != null) {
            this.title = title;
        }
        if (address != null) {
            this.address = address;
        }
        if (minPrice != null) {
            this.minPrice = minPrice;
        }
        if (openAt != null) {
            this.openAt = openAt;
        }
        if (closeAt != null) {
            this.closeAt = closeAt;
        } if (image != null) {
            this.imageUrl = image.getOriginalFilename();
        }
    }

    public Long getUserId() {
        return user.getId();
    }

    public String setImgUrl(String imgUrl) {
        return this.imageUrl = imgUrl;
    }
}
