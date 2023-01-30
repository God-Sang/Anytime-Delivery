package com.godsang.anytimedelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60)
@EnableCaching
@EnableJpaAuditing
public class AnytimedeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnytimedeliveryApplication.class, args);
    }
}
