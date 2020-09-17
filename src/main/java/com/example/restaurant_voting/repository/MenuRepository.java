package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.Menu;
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
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Caching(
            evict = {
                    @CacheEvict(value = "menusPage", allEntries = true),
                    @CacheEvict(value = "menusDatePage", allEntries = true),
                    @CacheEvict(value = "menus"),
                    @CacheEvict(value = "dishesPage", allEntries = true),
                    @CacheEvict(value = "dishesDatePage", allEntries = true),
                    @CacheEvict(value = "dishes", allEntries = true)
            }
    )
    @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    int delete(@Param("id") int id);

    @Cacheable(value = "menusDatePage", key = "{#date, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    @EntityGraph(value = "Menu.restaurant.dishes")
    @Query(value = "SELECT m FROM Menu m WHERE m.actionDate=:date", countQuery = "SELECT COUNT(m) FROM Menu m")
    Page<Menu> findByDateWithJoin(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Pageable pageable);

    @Cacheable(value = "menus")
    @EntityGraph(value = "Menu.restaurant.dishes")
    @Query("SELECT m FROM Menu m  WHERE m.id=:id")
    Optional<Menu> findByIdWithJoin(@Param("id") Integer id);

    @EntityGraph(value = "Menu.restaurant")
    @Query("SELECT m FROM Menu m WHERE m.id=:id")
    Optional<Menu> findByIdWithJoinRestaurant(@Param("id") Integer id);

    //    https://stackoverflow.com/questions/21549480/spring-data-fetch-join-with-paging-is-not-working
    //    https://stackoverflow.com/questions/26901010/spring-data-jpa-eager-fetch-with-join-and-using-pagination-not-working

    @Cacheable(value = "menusPage", key = "{#pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    @EntityGraph(value = "Menu.restaurant.dishes")
    @Query(value = "SELECT m FROM Menu m ", countQuery = "SELECT COUNT(m) FROM Menu m")
    Page<Menu> findAllWithJoin(Pageable pageable);

    @Caching(
            evict = {
                    // only creating
                    @CacheEvict(value = "menusPage", allEntries = true),
                    @CacheEvict(value = "menusDatePage", allEntries = true),
                    @CacheEvict(value = "menus", key = "#entity.id")
            }
    )
    @Transactional
    @Modifying
    @Override
    <S extends Menu> S save(S entity);
}
