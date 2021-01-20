package com.tc_4.carbon_counter.databases;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.tc_4.carbon_counter.models.DailyStats;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyStatsDatabase extends JpaRepository<DailyStats, Long> {
    List<DailyStats> findByUsername(String username);

    Optional<DailyStats> findTopByUsernameAndDateOrderByIdDesc(String username, LocalDate date);

    List<DailyStats> findByUsernameAndDateGreaterThanOrderByDateAsc(String username, LocalDate date);

}
