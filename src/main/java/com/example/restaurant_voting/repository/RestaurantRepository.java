package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.Restaurant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Caching(
            evict = {
                    @CacheEvict(value = "restaurantsPage", allEntries = true),
                    @CacheEvict(value = "restaurants"),
                    @CacheEvict(value = "menusPage", allEntries = true),
                    @CacheEvict(value = "menusDatePage", allEntries = true),
                    @CacheEvict(value = "menus", allEntries = true)
            }
    )
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    @Query(value = "SELECT r FROM Restaurant r WHERE LOWER(r.name) LIKE CONCAT('%',LOWER(:name),'%')",
            countQuery = "SELECT COUNT(r) FROM Restaurant r")
    Page<Restaurant> findByNameIgnoreCase(@Param("name") String name, Pageable pageable);

    @Cacheable(value = "restaurantsPage", key = "{#pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    @Override
    Page<Restaurant> findAll(Pageable pageable);

    @Cacheable(value = "restaurants")
    @Override
    Optional<Restaurant> findById(Integer integer);

    @Caching(
            evict = {
                    @CacheEvict(value = "restaurantsPage", allEntries = true),
                    @CacheEvict(value = "restaurants", key = "#entity.id"),
                    @CacheEvict(value = "menusPage", allEntries = true),
                    @CacheEvict(value = "menusDatePage", allEntries = true),
                    @CacheEvict(value = "menus", allEntries = true)
            }
    )
    @Transactional
    @Override
    <S extends Restaurant> S save(S entity);
}
