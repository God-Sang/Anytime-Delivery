package com.godsang.anytimedelivery.objectMapper;

import com.godsang.anytimedelivery.helper.cache.CacheUtils;
import com.godsang.anytimedelivery.helper.stub.StubData;
import com.godsang.anytimedelivery.store.entity.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ObjectMapperTest {
  @Autowired
  RedisCacheManager cacheManager;
  @Autowired
  RedisTemplate redisTemplate;

  @BeforeEach
  void clearCache() {
    CacheUtils.clearCache(redisTemplate);
    cacheManager.initializeCaches();
  }

  @Test
  void serializeTest() {
    // given
    int numberOfStores = 10;
    Page<Store> storePage = getStorePage(numberOfStores);
    String key = "testKey";
    Cache cache = cacheManager.getCache(key);

    // when
    cache.put(key, storePage);
    Page<Store> deserialized = (Page<Store>) cache.get(key).get();

    // then
    assertThat(deserialized.getContent().size())
        .isEqualTo(storePage.getContent().size());
    assertThat(deserialized.getSize()).isEqualTo(storePage.getSize());
    assertThat(deserialized.getNumber()).isEqualTo(storePage.getNumber());
    assertThat(deserialized.getTotalElements()).isEqualTo(storePage.getTotalElements());
  }

  private Page<Store> getStorePage(int num) {
    List<Store> stores = new ArrayList<>();
    IntStream.range(0, num).forEach(i -> stores.add(StubData.MockStore.getMockEntity()));
    return new PageImpl<>(stores);
  }
}
