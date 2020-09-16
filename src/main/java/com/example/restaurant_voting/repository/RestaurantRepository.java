package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    @Query(value = "SELECT r FROM Restaurant r WHERE LOWER(r.name) LIKE CONCAT('%',LOWER(:name),'%')",
            countQuery = "SELECT COUNT(r) FROM Restaurant r")
    Page<Restaurant> findByNameIgnoreCase(@Param("name") String name, Pageable pageable);
}
