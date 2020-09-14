package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d  WHERE d.id IN (SELECT d.id FROM Dish d INNER JOIN d.menu m " +
            "WHERE d.id=:id and m.actionDate =:date)")
    int delete(@Param("id") int id, @Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

    @EntityGraph(value = "Dish.menu")
    @Query(value = "select d from Dish d where d.menu.actionDate=:date", countQuery = "SELECT count(d) FROM Dish d")
    Page<Dish> findByDate(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Pageable pageable);

    @EntityGraph(value = "Dish.menu")
    @Query(value = "SELECT d FROM Dish d WHERE LOWER(d.name) LIKE CONCAT('%',LOWER(:name),'%')",
            countQuery = "SELECT count(d) FROM Dish d")
    Page<Dish> findByNameIgnoreCase(@Param("name") String name, Pageable pageable);

    @EntityGraph(value = "Dish.menu")
    @Query("select d from Dish d where d.id = :id")
    Optional<Dish> findByIdWithJoin(@Param("id") int id);

    @EntityGraph(value = "Dish.menu")
    @Query(value = "SELECT d FROM Dish d ", countQuery = "SELECT count(d) FROM Dish d")
    Page<Dish> findAllWithJoin(Pageable pageable);

}
