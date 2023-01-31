package com.godsang.anytimedelivery.categoriy;

import com.godsang.anytimedelivery.category.controller.CategoryController;
import com.godsang.anytimedelivery.category.repository.CategoryRepository;
import com.godsang.anytimedelivery.category.service.CategoryService;
import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.stream.IntStream;


import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class serviceCacheTest {
  @MockBean
  private CategoryRepository categoryRepository;
  @Autowired
  private CategoryService categoryService;
  //TODO: 캐시용 레디스를 먼저 지워야함.
  @Test
  public void getAllCategoriesTest() {
    given(categoryRepository.findAll()).willReturn(new ArrayList<>());

    IntStream.range(0, 10)
        .forEach(i -> categoryService.getAllCategories());

    then(categoryRepository).should().findAll();
  }
}
