package com.godsang.anytimedelivery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.time.Duration;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600)
@EnableCaching
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

  /**
   * Cache Manager for Redis Cache
   * defaultCacheConfig: default 설정을 사용, 추가적으로 customizing
   * disableCachingNullValues: null값 caching 불가
   * entryTtl: cache의 Time To Live 설정
   * serializeKeysWith: key를 직렬화, 역직렬화할 때 사용할 serializer 설정
   * serializeValuesWith: value를 직렬화, 역직렬화할 때 사용할 serializer 설정
   * default valueSerializer는 'JdkSerializationRedisSerializer'이지만 human-readable하지 못함
   * json format이고 다양한 class type으로 직렬화할 수 있는 GenericJackson2JsonRedisSerializer 사용
   */
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
        .entryTtl(Duration.ofSeconds(3600));

    return RedisCacheManager.RedisCacheManagerBuilder
        .fromConnectionFactory(redisCacheConnectionFactory())
        .cacheDefaults(redisCacheConfiguration)
        .build();
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisCacheConnectionFactory());
    return redisTemplate;
  }
}
