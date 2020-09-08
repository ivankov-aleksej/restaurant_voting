package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT d FROM Dish d WHERE d.menu.actionDate=:date")
    Page<Dish> findByDate(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Pageable pageable);

    @Query("SELECT d FROM Dish d WHERE LOWER(d.name) LIKE CONCAT('%',LOWER(:name),'%')")
    Page<Dish> findByNameIgnoreCase(@Param("name") String name, Pageable pageable);
}
