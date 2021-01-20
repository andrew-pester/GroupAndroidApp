package com.tc_4.carbon_counter.databases;

import java.util.List;
import java.util.Optional;

import com.tc_4.carbon_counter.models.User;
import com.tc_4.carbon_counter.models.User.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDatabase extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    List<User> findByRole(Role role);

    boolean existsByUsername(String username);

    List<User> findAll();
}
