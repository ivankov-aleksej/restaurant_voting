package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);

    @Cacheable(value = "users")
    Optional<User> findByEmail(String email);

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @Override
    <S extends User> S save(S entity);
}
