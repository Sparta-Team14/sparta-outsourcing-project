package com.example.jeogiyoproject.domain.menu.repository;

import com.example.jeogiyoproject.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu,Long> {
}
