package com.godsang.anytimedelivery.store.service;

import com.godsang.anytimedelivery.auth.utils.LoggedInUserInfoUtils;
import com.godsang.anytimedelivery.category.entity.Category;
import com.godsang.anytimedelivery.category.entity.CategoryStore;
import com.godsang.anytimedelivery.category.service.CategoryService;
import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryAreaStore;
import com.godsang.anytimedelivery.deliveryArea.service.DeliveryAreaService;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service logics for Stores
 */
@Service
@RequiredArgsConstructor
public class StoreService {
  private final StoreRepository storeRepository;
  private final CategoryService categoryService;
  private final DeliveryAreaService deliveryAreaService;
  private final LoggedInUserInfoUtils loggedInUserInfoUtils;
  private final UserService userService;

  /**
   * search stores by category id and page information.
   *
   * @param categoryId 카테고리 아이디
   * @param pageable   페이지 정보
   * @return Store Page
   */
  public Page<Store> findStoreByCategoryId(Long categoryId, Pageable pageable) {
    categoryService.findVerifiedCategoryById(categoryId);
    return storeRepository.findStoresByCategory(categoryId, pageable);
  }

  /**
   * 가게 등록
   *
   * @Param categoryIds 카테고리 아이디
   * @Param deliveryAreas 배달 가능 지역
   * @Return Store
   */
  @Transactional
  public Store createStore(List<Long> categoryIds, List<String> deliveryAreas, Store store) {
    User user = userService.findUser(loggedInUserInfoUtils.extractUserId());
    store.setUser(user);
    verifyDuplicatedStoreInfo(store.getRegistrationNumber(), store.getTel(), store.getAddress(), store.getName());
    setCategoryStore(store, categoryIds);
    setDeliveryAreaStore(store, deliveryAreas);
    return storeRepository.save(store);
  }

  /**
   * category와 N:M 매핑
   */
  private void setCategoryStore(Store store, List<Long> categoryIds) {
    for (long categoryId : categoryIds) {
      Category category = categoryService.findVerifiedCategoryById(categoryId);
      CategoryStore categoryStore = new CategoryStore(category, store);
      store.addCategoryStore(categoryStore);
    }
  }

  /**
   * delivery area와 N:M 매핑
   */
  private void setDeliveryAreaStore(Store store, List<String> deliveryAreas) {
    for (String juso : deliveryAreas) {
      DeliveryArea deliveryArea = deliveryAreaService.findExistedDeliveryArea(juso);
      DeliveryAreaStore deliveryAreaStore = new DeliveryAreaStore(store, deliveryArea);
      store.addDeliveryAreaStore(deliveryAreaStore);
    }
  }

  /**
   * 가게 등록 시 unique 필드 중복 확인
   */
  private void verifyDuplicatedStoreInfo(String registrationNumber, String tel, String address, String name) {
    verifyRegistrationNumberExists(registrationNumber);
    verifyTelExists(tel);
    verifyAddressExists(address);
    verifyNameExists(name);
  }

  /**
   * 사업자 등록번호 중복 확인
   *
   * @throws BusinessLogicException when same registration number found
   */
  private void verifyRegistrationNumberExists(String registrationNumber) {
    if (storeRepository.existsByRegistrationNumber(registrationNumber)) {
      throw new BusinessLogicException(ExceptionCode.REGISTRATION_NUMBER_ALREADY_EXISTS);
    }
  }

  /**
   * 전화번호 중복 확인
   *
   * @throws BusinessLogicException when same tel found
   */
  private void verifyTelExists(String tel) {
    if (storeRepository.existsByTel(tel)) {
      throw new BusinessLogicException(ExceptionCode.TEL_ALREADY_EXISTS);
    }
  }

  /**
   * 주소 중복 확인
   *
   * @throws BusinessLogicException when same address found
   */
  private void verifyAddressExists(String address) {
    if (storeRepository.existsByAddress(address)) {
      throw new BusinessLogicException(ExceptionCode.ADDRESS_ALREADY_EXISTS);
    }
  }

  /**
   * 가게명 중복 확인
   *
   * @throws BusinessLogicException when same name found
   */
  private void verifyNameExists(String name) {
    if (storeRepository.existsByName(name)) {
      throw new BusinessLogicException(ExceptionCode.NAME_ALREADY_EXISTS);
    }
  }

  /**
   * store id로 store를 찾는다.
   *
   * @Param storeId store id
   * @Return Store
   */
  public Store findStoreById(long storeId) {
    Optional<Store> store = storeRepository.findById(storeId);
    return store.orElseThrow(() -> new BusinessLogicException(ExceptionCode.STORE_NOT_FOUND));
  }
}
