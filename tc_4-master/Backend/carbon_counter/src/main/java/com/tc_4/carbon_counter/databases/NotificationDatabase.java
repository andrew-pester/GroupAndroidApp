package com.tc_4.carbon_counter.databases;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import com.tc_4.carbon_counter.models.Notification;

public interface NotificationDatabase extends JpaRepository<Notification, Long> {
 
    Optional<Notification> findById (Long id);

    Optional<Notification> findByUsername(String username);

    List<Notification> findByUsernameAndIsRead(String username, boolean isRead);
}