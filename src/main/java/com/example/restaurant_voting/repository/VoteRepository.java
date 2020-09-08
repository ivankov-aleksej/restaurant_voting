package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId")
    Page<Vote> findAll(Pageable pageable, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v WHERE v.date=:date AND v.user.id=:userId")
    List<Vote> findByDate(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          @Param("userId") int userId);
}
