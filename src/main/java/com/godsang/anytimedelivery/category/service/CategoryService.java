package com.godsang.anytimedelivery.category.service;

import com.godsang.anytimedelivery.category.entity.Category;
import com.godsang.anytimedelivery.category.repository.CategoryRepository;
import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * CRUD for Categories. Only admin can change data.
 * Category data are saved in Cache as well as in RDB.
 */

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  /**
   * get list of all categories
   */
  @Cacheable(cacheNames = "categories", key = "'categories'")
  @Transactional(readOnly = true)
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  /**
   * Add new category.
   *
   * @throws BusinessLogicException when same category found.
   */
  @CacheEvict(cacheNames = "categories", key = "'categories'")
  @Transactional
  public Category addCategory(String name) {
    Category category = new Category(name);
    checkDuplicate(name);
    return categoryRepository.save(category);
  }

  /**
   * update a specific category after duplication check
   */
  @CacheEvict(cacheNames = "categories", key = "'categories'")
  @Transactional
  public Category updateCategory(String prevName, String curName) {
    Category category = findVerifiedCategoryByName(prevName);
    checkDuplicate(curName);
    category.changeName(curName);
    return categoryRepository.save(category);
  }

  /**
   * delete a specific category
   */
  @CacheEvict(cacheNames = "categories", key = "'categories'")
  @Transactional
  public void deleteCategory(String name) {
    Category category = findVerifiedCategoryByName(name);
    categoryRepository.delete(category);
  }

  /**
   * validate id of category
   */
  @Transactional(readOnly = true)
  public Category findVerifiedCategoryById(Long categoryId) {
    Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
    return optionalCategory.orElseThrow(() -> new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
  }

  private Category findVerifiedCategoryByName(String name) {
    Optional<Category> optionalCategory = categoryRepository.findByName(name);
    return optionalCategory.orElseThrow(() -> new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
  }

  private void checkDuplicate(String name) {
    Optional<Category> optionalCategory = categoryRepository.findByName(name);
    if (optionalCategory.isPresent()) {
      throw new BusinessLogicException(ExceptionCode.CATEGORY_ALREADY_EXISTS);
    }
  }
}
