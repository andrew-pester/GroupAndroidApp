package com.tc_4.carbon_counter.databases;

import java.util.List;
import java.util.Optional;

import com.tc_4.carbon_counter.models.Tip;
import com.tc_4.carbon_counter.models.Tip.Category;
import com.tc_4.carbon_counter.models.Tip.Status;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TipsDatabase extends JpaRepository<Tip, Long> {
    
    Optional<Tip> findByTitle(String title);
    Optional<Tip> findByTitleAndStatus(String title, Status status);
    Optional<Tip> findByWorkingTitleAndStatus(String title, Status status);
    Optional<Tip> findByWorkingTitle(String workingTitle);
    List<Tip> findByCategoryAndStatus(Category category,Status status);
    List<Tip> findByStatus(Status status);
    Optional<Tip> findById(Long id);
}
