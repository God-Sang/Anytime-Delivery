package com.godsang.anytimedelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AnytimedeliveryApplication {
  public static void main(String[] args) {
    SpringApplication.run(AnytimedeliveryApplication.class, args);
  }
}
