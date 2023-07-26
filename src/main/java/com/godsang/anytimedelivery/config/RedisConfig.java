package com.godsang.anytimedelivery.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.godsang.anytimedelivery.config.objectMapper.PageDeserializer;
import com.godsang.anytimedelivery.config.objectMapper.PageSerializer;
import com.godsang.anytimedelivery.config.objectMapper.StoreMixin;
import com.godsang.anytimedelivery.store.entity.Store;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
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
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600)
@EnableCaching
public class RedisConfig {
  @Value("${spring.redis.session.host}")
  private String host;
  @Value("${spring.redis.session.port}")
  private int sessionPort;
  @Value("${spring.redis.cache.port}")
  private int cachePort;

  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.addMixIn(Store.class, StoreMixin.class);

    GenericJackson2JsonRedisSerializer.registerNullValueSerializer(mapper, null);
    StdTypeResolverBuilder builder = new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.EVERYTHING,
        mapper.getPolymorphicTypeValidator());
    builder = builder.init(JsonTypeInfo.Id.CLASS, null);
    builder = builder.inclusion(JsonTypeInfo.As.PROPERTY);
    mapper.setDefaultTyping(builder);
    Module module = new SimpleModule()
        .addSerializer(PageImpl.class, new PageSerializer())
        .addDeserializer(PageImpl.class, new PageDeserializer());
    mapper.registerModule(module);
    return mapper;
  }

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
   * LocalDataType 직렬화/역직렬화 지원을 위해 Custom Object Mapper를 구성해서 GenericJackson2JsonRedisSerializer에 등록
   */
  @Bean
  public RedisCacheManager redisCacheManager(ObjectMapper objectMapper) {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
        .defaultCacheConfig()
        .disableCachingNullValues()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper))
        )
        .entryTtl(Duration.ofHours(6L));

    return RedisCacheManager.RedisCacheManagerBuilder
        .fromConnectionFactory(redisCacheConnectionFactory())
        .cacheDefaults(redisCacheConfiguration)
        .cacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(
            redisCacheConnectionFactory(), BatchStrategies.scan(1000)))
        .build();
  }

  @Bean
  public CacheResolver storeCacheResolver(RedisCacheManager cacheManager) {
    return new StoreCacheResolver(cacheManager);
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate() {
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    redisTemplate.setDefaultSerializer(new StringRedisSerializer());
    redisTemplate.setConnectionFactory(redisCacheConnectionFactory());
    return redisTemplate;
  }
}
