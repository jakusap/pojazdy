package com.example.pojazdy

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.client.RestTemplate

@SpringBootApplication
class PojazdyApplication {

    static void main(String[] args) {
        SpringApplication.run(PojazdyApplication, args)
    }

    @Bean
    RestTemplate restTemplate() {
        new RestTemplate()
    }
}
