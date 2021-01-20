package com.tc_4.carbon_counter.security;


import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.tc_4.carbon_counter.databases.UserDatabase;
import com.tc_4.carbon_counter.services.CarbonUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

/**
 * Custom configuration class that sets up security for the entire project.
 * Using basic HTTP authentication 
 * 
 * @author Colton Glick
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDatabase userDatabase;
    @Autowired
    private WebApplicationContext applicationContext;
    @Autowired
    private CarbonUserDetailsService userDetailsService;
    @Autowired 
    private DataSource datasource;

    /**
     * Assign the custom UserDetailsService
     */
    @PostConstruct
    public void completeSetup() {
        userDetailsService = applicationContext.getBean(CarbonUserDetailsService.class);
    }
    
    /**
     * Defines settings for the project. Using HTTP basic authentication.
     * Also defines which pages do and dont require authentication
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
        .csrf().disable()
        .authorizeRequests()
            .antMatchers(
                "/", 
                "/user/add", 
                "/tip/{title}",
                "/tip/addTip",
                "/tips/{catagory}",
                "/tips/all", 
                "/user/friend_request/{user}",
                //allow API docs
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                //websocket
                "/notify/**"
                ).permitAll()
            .anyRequest().authenticated()            
        .and().httpBasic();
        //need authenication to edit tips actually just need to test
    }

    /**
     * Configure additional settings to use the custom UserDetailsService
     * for authentication. Also assigns an encoder to user for encrypting 
     * user passwords.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(encoder())
            .and()
            .authenticationProvider(authenticationProvider())
            .jdbcAuthentication()
            .dataSource(datasource);
    }

    /**
     * Generates the authentication provider for the configure global method
     * @return the DaoAuthenticationProvider for the custom UserDetailsService
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    /**
     * Generates a new encoder to encrypt passwords
     * 
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder(11);
    }
}
