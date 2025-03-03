package com.example.jeogiyoproject.domain.menu.entity;

import com.example.jeogiyoproject.domain.base.BaseEntity;
import com.example.jeogiyoproject.domain.menu.dto.menu.MenuUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "menu")
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public void updateMenu(MenuUpdateRequestDto requestDto){
        if(requestDto.getName() != null){
            this.name = requestDto.getName();
        }
        if(requestDto.getInfo() != null){
            this.info = requestDto.getInfo();
        }
        if(requestDto.getPrice() != null){
            this.price = requestDto.getPrice();
        }
    }

    public void setDeletedAt(){
        this.deletedAt = LocalDateTime.now();
    }

    public void restore(){
        this.deletedAt = null;
    }
}
