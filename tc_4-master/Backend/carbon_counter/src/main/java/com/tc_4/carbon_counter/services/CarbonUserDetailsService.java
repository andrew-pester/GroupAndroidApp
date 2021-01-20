package com.tc_4.carbon_counter.services;

import java.util.Optional;

import com.tc_4.carbon_counter.databases.UserDatabase;
import com.tc_4.carbon_counter.models.User;
import com.tc_4.carbon_counter.security.CarbonUserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service to allow for database backed authentication. Defines how to retrieve user's
 * credentials from the user database.
 * 
 * @author Colton Glick
 */
@Service
public class CarbonUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserDatabase userDatabase;

    @Override
    public UserDetails loadUserByUsername(String username){
        Optional<User> user = userDatabase.findByUsername(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException(username);
        } 
        return new CarbonUserPrincipal(user.get());
    }
}
