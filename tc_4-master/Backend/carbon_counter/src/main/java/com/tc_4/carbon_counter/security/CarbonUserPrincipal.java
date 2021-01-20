package com.tc_4.carbon_counter.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.tc_4.carbon_counter.models.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Custom authentication class that allows for database backed authentication.
 * This class defines how to get various details about the user executing functions.
 * 
 * @author Colton Glick
 */
public class CarbonUserPrincipal implements UserDetails {
    private User user;
    
    public CarbonUserPrincipal(User user){
        this.user = user;
    }

    @Override
    public String getUsername(){
        return user.getUsername();
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * @return the current authenticated user
     */
    public User getUser(){
        return user;
    }
    
}
