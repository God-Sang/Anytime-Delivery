package com.godsang.anytimedelivery.category;

import com.godsang.anytimedelivery.category.entity.Category;
import com.godsang.anytimedelivery.category.repository.CategoryRepository;
import com.godsang.anytimedelivery.category.service.CategoryService;
import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ServiceCacheTest {
  @MockBean
  private CategoryRepository categoryRepository;
  @Autowired
  private CategoryService categoryService;

  /**
   * 먼저 한번 호출을 하여, 캐시에 저장
   */
  @BeforeEach
  void saveInCache() {
    categoryService.getAllCategories();
  }

  @Test
  @DisplayName("캐시에서 바로 리턴, DB 접근 안함.")
  void getAllCategoriesTest() {
    given(categoryRepository.findAll()).willReturn(new ArrayList<>());

    IntStream.range(0, 10)
        .forEach(i -> categoryService.getAllCategories());

    then(categoryRepository).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("존재하는 카테고리를 추가할 경우")
  void addCategoryExceptionTest() {
    String newCategoryName = "피자";
    given(categoryRepository.findByName(any())).willReturn(Optional.of(new Category("피자")));

    Assertions.assertThrows(BusinessLogicException.class, () -> {
      categoryService.addCategory(newCategoryName);
    });
  }

  @Test
  @DisplayName("추가에 실패할 경우 cacheEvict 작동 안함.")
  void addCategoryExceptionCacheTest() {
    // given
    String newCategoryName = "피자";
    given(categoryRepository.findByName(any())).willReturn(Optional.of(new Category("피자")));

    // when
    Assertions.assertThrows(BusinessLogicException.class, () -> {
      categoryService.addCategory(newCategoryName);
    });
    categoryService.getAllCategories();
    //then
    verify(categoryRepository, times(0)).findAll();
  }

  @Test
  @DisplayName("추가에 성공할 경우 cacheEvict 작동.")
  void addCategorySuccessCacheTest() {
    // given
    String newCategoryName = "핏짜";
    given(categoryRepository.findByName(any())).willReturn(Optional.empty());

    // when
    categoryService.addCategory(newCategoryName);
    categoryService.getAllCategories();

    //then
    verify(categoryRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("update : 바꾸고자하는 카테고리가 없는 경우")
  void updateCategoryFailTest() {
    // given
    String from = "핏짜";
    String to = "피자";
    given(categoryRepository.findByName(any())).willReturn(Optional.empty());

    // when then
    Assertions.assertThrows(BusinessLogicException.class, () -> {
      categoryService.updateCategory(from, to);
    });
  }

  @Test
  @DisplayName("update : 바꿀 대상이 이미 존재하는 경우")
  void updateCategoryFailTest2() {
    // given
    String from = "핏짜";
    String to = "피자";
    given(categoryRepository.findByName(from)).willReturn(Optional.of(new Category(from)));
    given(categoryRepository.findByName(to)).willReturn(Optional.of(new Category(to)));
    // when then
    Assertions.assertThrows(BusinessLogicException.class, () -> {
      categoryService.updateCategory(from, to);
    });
  }

  @Test
  @DisplayName("udpate : 성공하는 경우, cacheEvict 작동")
  void updateCategorySuccessTest2() {
    // given
    String from = "핏짜";
    String to = "피자";
    given(categoryRepository.findByName(from)).willReturn(Optional.of(new Category(from)));
    given(categoryRepository.findByName(to)).willReturn(Optional.empty());
    // when
    categoryService.updateCategory(from, to);
    categoryService.getAllCategories();
    //then
    verify(categoryRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("delete : 삭제하려는 값이 없는 경우")
  void deleteCategoryFailTest() {
    // given
    String name = "피자";
    given(categoryRepository.findByName(name)).willReturn(Optional.empty());
    // when then
    Assertions.assertThrows(BusinessLogicException.class, () -> {
          categoryService.deleteCategory(name);
    });
  }

  @Test
  @DisplayName("delete : 성공하는 경우")
  void deleteCategorySuccessTest() {
    // given
    String name = "피자";
    given(categoryRepository.findByName(name)).willReturn(Optional.of(new Category(name)));
    // when then
    Assertions.assertDoesNotThrow(() -> categoryService.deleteCategory(name));
  }
}
