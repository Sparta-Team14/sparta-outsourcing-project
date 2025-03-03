package com.example.jeogiyoproject.domain.menu.entity;

import com.example.jeogiyoproject.domain.base.BaseEntity;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "menu_category")
@NoArgsConstructor
public class MenuCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "foodstore_id")
    private FoodStore foodStore;

    @OneToMany(mappedBy = "menuCategory",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @Column
    private LocalDateTime deletedAt;

    public MenuCategory(String name, FoodStore foodStore) {
        this.name = name;
        this.foodStore = foodStore;
    }
}
