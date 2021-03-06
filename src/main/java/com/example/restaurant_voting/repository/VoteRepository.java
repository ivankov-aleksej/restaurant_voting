package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.Vote;
import com.example.restaurant_voting.to.VoteTo;
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
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.user.id=:userId AND v.date=:date")
    int delete(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @Param("userId") int userId);

    @EntityGraph(value = "Vote.menu.restaurant")
    @Query(value = "SELECT v FROM Vote v WHERE v.user.id=:userId", countQuery = "SELECT COUNT(v) FROM Vote v")
    Page<Vote> findAll(Pageable pageable, @Param("userId") int userId);

    @EntityGraph(value = "Vote.menu.restaurant")
    @Query("SELECT v FROM Vote v WHERE v.date=:date AND v.user.id=:userId")
    List<Vote> findByDateJoin(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v WHERE v.date=:date AND v.user.id=:userId")
    List<Vote> findByDate(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          @Param("userId") int userId);

    @Query(value = "SELECT new com.example.restaurant_voting.to.VoteTo(v.date, v.menu.id , v.menu.restaurant.id, v.menu.restaurant.name, v.menu.actionDate, COUNT(v.user.id))" +
            " FROM Vote v  WHERE v.date=:date GROUP BY v.menu.id", countQuery = "SELECT COUNT(v) FROM Vote v")
    Page<VoteTo> getStatisticByDate(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                    Pageable pageable);

    @Query(value = "SELECT new com.example.restaurant_voting.to.VoteTo(v.date, v.menu.id , v.menu.restaurant.id, v.menu.restaurant.name, v.menu.actionDate, COUNT(v.user.id)) " +
            "FROM Vote v  GROUP BY v.date, v.menu.id", countQuery = "SELECT COUNT(v) FROM Vote v")
    Page<VoteTo> getStatisticAll(Pageable pageable);
}
