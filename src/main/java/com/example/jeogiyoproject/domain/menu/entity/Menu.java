package com.example.jeogiyoproject.domain.menu.entity;

import com.example.jeogiyoproject.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "menu")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="menu_category_id")
    private MenuCategory menuCategory;

    @Column
    private String name;

    @Column
    private String info;

    @Column
    private Integer price;

    @Column
    private LocalDateTime deletedAt;

    public Menu(MenuCategory menuCategory, String name, String info, Integer price) {
        this.menuCategory = menuCategory;
        this.name = name;
        this.info = info;
        this.price = price;
    }
}
