package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @EntityGraph(value = "Menu.restaurant.dishes")
    @Query(value = "SELECT m FROM Menu m WHERE m.actionDate=:date", countQuery = "SELECT count(m) FROM Menu m")
    Page<Menu> findByDateWithJoin(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Pageable pageable);

    @EntityGraph(value = "Menu.restaurant.dishes")
    @Query("SELECT m FROM Menu m  WHERE m.id=:id")
    Optional<Menu> findByIdWithJoin(@Param("id") Integer id);

    @EntityGraph(value = "Menu.restaurant")
    @Query("SELECT m FROM Menu m WHERE m.id=:id")
    Optional<Menu> findByIdWithJoinRestaurant(@Param("id") Integer id);

    //    https://stackoverflow.com/questions/21549480/spring-data-fetch-join-with-paging-is-not-working
    //    https://stackoverflow.com/questions/26901010/spring-data-jpa-eager-fetch-with-join-and-using-pagination-not-working

    @EntityGraph(value = "Menu.restaurant.dishes")
    @Query(value = "SELECT m FROM Menu m ", countQuery = "SELECT count(m) FROM Menu m")
    Page<Menu> findAllWithJoin(Pageable pageable);
}
