package com.godsang.anytimedelivery.store;

import com.godsang.anytimedelivery.category.entity.CategoryStore;
import com.godsang.anytimedelivery.category.service.CategoryService;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryAreaStore;
import com.godsang.anytimedelivery.helper.cache.CacheUtils;
import com.godsang.anytimedelivery.helper.stub.StubData;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class RepositoryCacheTest {
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Test
  void cacheEvictTest() {
    // given

    // given
    // cache 생성
    // 1) "storeCa1De1::page3size10"
    // 2) "storeCa2De2::page3size10"
    // 3) "storeCa3De3::page3size10"
    // 4) "storeCa4De4::page3size10"
    // 5) "storeCa5De5::page3size10"
    ValueOperations<String, String> operations = redisTemplate.opsForValue();
    IntStream.range(1, 6).forEach(id -> {
      operations.set(String.format("storeCa%dDe%d::page3size10", id, id), "someObject");
    });

    // when
    // evict
    // 1) "storeCa1De1::page3size10"
    // 2) "storeCa2De2::page3size10"
    // 3) "storeCa3De3::page3size10"
    Store store = StubData.MockStore.getMockEntityWithDeliveryAreaAndCategory();
    List<CategoryStore> cs = store.getCategoryStores();
    for (int i = 0; i < cs.size(); i++) {
      Long categoryId = (long) (i + 1);
      ReflectionTestUtils.setField(cs.get(i).getCategory(), "categoryId", categoryId);
    }
    List<DeliveryAreaStore> das = store.getDeliveryAreaStores();
    for (int i = 0; i < das.size(); i++) {
      Long deliveryAreaId = (long) (i + 1);
      ReflectionTestUtils.setField(das.get(i).getDeliveryArea(), "deliveryAreaId", deliveryAreaId);
    }
    storeRepository.save(store);

    // then 최종 남은 캐시는 2개
    Set<String> keys = redisTemplate.keys("*");
    assertThat(keys.contains("storeCa1De1::page3size10")).isFalse();
    assertThat(keys.contains("storeCa2De2::page3size10")).isFalse();
    assertThat(keys.contains("storeCa3De3::page3size10")).isFalse();
    assertThat(keys.contains("storeCa4De4::page3size10")).isTrue();
    assertThat(keys.contains("storeCa5De5::page3size10")).isTrue();

    CacheUtils.clearCache(redisTemplate);
  }
}
