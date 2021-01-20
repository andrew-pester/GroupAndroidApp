package com.example.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EnableJpaRepositories("com.baeldung.persistence.repo") 
@EntityScan("com.baeldung.persistence.model")
@SpringBootApplication 

public class Application {
    spring.datasource.driver-class-name=org.h2.Driver
    spring.datasource.url=jdbc:h2:mem:bootapp;DB_CLOSE_DELAY=-1
    spring.datasource.username=sa
    spring.datasource.password=
}