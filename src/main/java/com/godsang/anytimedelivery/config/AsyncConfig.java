package com.godsang.anytimedelivery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

  /**
   * CorePoolSize: 기본 실행 Thread 수
   * MaxPoolSize: 최대 Thread 수
   * QueueCapacity: 최대 수용 가능한 Queue 수
   * ThreadNamePrefix: Thread 접두사
   * Pool size가 가득 찬 상태에서 Queue의 size도 가득 차게되면 Pool size 증가 -> MaxPoolSize를 초과하면 rejection policy에 따라 handling
   * Ideal thread pool size = Number of CPU cores * (1 + Wait time / Service time)
   */
  @Bean
  public Executor asyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(30);
    executor.setQueueCapacity(15);
    executor.setThreadNamePrefix("anytime-exec-");
    executor.initialize();
    return executor;
  }
}
