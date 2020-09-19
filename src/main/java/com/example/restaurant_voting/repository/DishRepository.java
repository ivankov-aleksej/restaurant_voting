package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.Dish;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

    @Caching(
            evict = {
                    @CacheEvict(value = "dishesPage", allEntries = true),
                    @CacheEvict(value = "dishesDatePage", allEntries = true),
                    @CacheEvict(value = "dishes"),
                    @CacheEvict(value = "menusPage", allEntries = true),
                    @CacheEvict(value = "menusDatePage", allEntries = true),
                    @CacheEvict(value = "menus", allEntries = true)
            }
    )
    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d  WHERE d.id = :id")
    int delete(@Param("id") int id);

    @Cacheable(value = "dishesDatePage", key = "{#date, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    @EntityGraph(value = "Dish.menu")
    @Query(value = "SELECT d FROM Dish d WHERE d.menu.actionDate=:date", countQuery = "SELECT COUNT(d) FROM Dish d")
    Page<Dish> findByDate(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Pageable pageable);

    @EntityGraph(value = "Dish.menu")
    @Query(value = "SELECT d FROM Dish d WHERE LOWER(d.name) LIKE CONCAT('%',LOWER(:name),'%')",
            countQuery = "SELECT COUNT(d) FROM Dish d")
    Page<Dish> findByNameIgnoreCase(@Param("name") String name, Pageable pageable);

    @Cacheable(value = "dishes")
    @EntityGraph(value = "Dish.menu")
    @Query("SELECT d FROM Dish d WHERE d.id = :id")
    Optional<Dish> findByIdWithJoin(@Param("id") int id);

    @Cacheable(value = "dishesPage", key = "{#pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    @EntityGraph(value = "Dish.menu")
    @Query(value = "SELECT d FROM Dish d ", countQuery = "SELECT COUNT(d) FROM Dish d")
    Page<Dish> findAllWithJoin(Pageable pageable);

    @Caching(
            evict = {
                    @CacheEvict(value = "dishesPage", allEntries = true),
                    @CacheEvict(value = "dishesDatePage", allEntries = true),
                    @CacheEvict(value = "dishes", key = "#entity.id"),
                    @CacheEvict(value = "menusPage", allEntries = true),
                    @CacheEvict(value = "menusDatePage", allEntries = true),
                    @CacheEvict(value = "menus", key = "#entity.menu.id")
            }
    )
    @Transactional
    @Override
    <S extends Dish> S save(S entity);

}
