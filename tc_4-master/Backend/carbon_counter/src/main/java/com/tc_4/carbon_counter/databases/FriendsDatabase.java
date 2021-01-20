package com.tc_4.carbon_counter.databases;

import java.util.List;
import java.util.Optional;

import com.tc_4.carbon_counter.models.Friends;
import com.tc_4.carbon_counter.models.Friends.Status;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsDatabase extends JpaRepository<Friends, Long> {
    
    
    List<Friends> findByUserTwoAndStatus(String username, Status status);
    List<Friends> findByUserOneAndStatus(String username, Status status);
    Optional<Friends> findByUserOneAndUserTwo(String userOne, String userTwo);


    
}
