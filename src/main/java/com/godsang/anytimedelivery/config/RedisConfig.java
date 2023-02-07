package com.godsang.anytimedelivery.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.time.Duration;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60)
@EnableCaching
@Profile("!signupTest")
public class RedisConfig {
  @Value("${spring.redis.host}")
  private String host;
  @Value("${spring.redis.session.port}")
  private int sessionPort;
  @Value("${spring.redis.cache.port}")
  private int cachePort;

  /**
   * Session용 redis connection
   */
  @Bean({"redisConnectionFactory", "redisSessionConnectionFactory"})
  public RedisConnectionFactory redisSessionConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(host);
    redisStandaloneConfiguration.setPort(sessionPort);
    return new LettuceConnectionFactory(redisStandaloneConfiguration);
  }

  /**
   * Cache용 redis connection
   * CacheManager를 구현해야 한다.
   */
  @Bean
  public RedisConnectionFactory redisCacheConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(host);
    redisStandaloneConfiguration.setPort(cachePort);
    return new LettuceConnectionFactory(redisStandaloneConfiguration);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(ObjectMapper objectMapper) {
    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisCacheConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(serializer);
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(serializer);
    redisTemplate.setEnableTransactionSupport(true);
    return redisTemplate;
  }

  @Bean
  public RedisCacheManager redisCacheManager() {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
        .defaultCacheConfig()
        .disableCachingNullValues()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer())
        )
        .entryTtl(Duration.ofDays(3L));

    return RedisCacheManager.RedisCacheManagerBuilder
        .fromConnectionFactory(redisCacheConnectionFactory())
        .cacheDefaults(redisCacheConfiguration)
        .build();
  }
}
