package com.godsang.anytimedelivery.store;

import com.godsang.anytimedelivery.category.entity.Category;
import com.godsang.anytimedelivery.category.repository.CategoryRepository;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class storeRepositoryTest {
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  void associationTest() {
    //given
    Category category = categoryRepository.findById(1L).get();
    Store store = Store.builder()
        .address("aa")
        .close_time(LocalTime.now())
        .open_time(LocalTime.now())
        .delivery_fee(0)
        .name("피자헬")
        .tel("1234")
        .registrationNumber("1234")
        .category(category)
        .build();

    //when
    storeRepository.save(store);
    Category categoryAfter = categoryRepository.findById(1L).get();
    Store storeAfter = categoryAfter.getStores().get(0);
    //then
    assertThat(storeAfter.getName())
        .isEqualTo(store.getName());
  }
}
