package com.tc_4.carbon_counter.databases;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.tc_4.carbon_counter.models.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsDatabase extends JpaRepository<News, Long> {
    
    Optional<News> findByTitle(String title);

    Optional<News> findById (Long id);

    List<News> findByDateGreaterThanOrderByDateAsc(LocalDate date);

    List<News> findAll();

    List<News> findAllByOrderByDateAsc();
}
