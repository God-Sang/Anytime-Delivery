package com.godsang.anytimedelivery.helper.cache;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

public class CacheUtils {
  static public void clearCache(RedisTemplate<String, String> redisTemplate) {
    Set<String> keys = redisTemplate.keys("*");
    for (String key : keys) {
      redisTemplate.delete(key);
    }
  }
}
