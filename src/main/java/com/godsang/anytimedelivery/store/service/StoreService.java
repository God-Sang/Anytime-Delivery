package com.godsang.anytimedelivery.store.service;

import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.address.service.AddressService;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
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
  private final UserService userService;
  private final AddressService addressService;

  /**
   * search stores by category id and deliveryArea id and page information.
   * <p>
   *
   * @param categoryId 카테고리 아이디
   * @param userId     유저 아이디
   * @param page       페이지 번호
   * @param size       페이지 크기
   * @return Store page
   */
  @Transactional(readOnly = true)
  public Page<Store> findStores(Long categoryId, Long userId, int page, int size) {
    Address userAddress = addressService.getAddress(userId);
    Long deliveryAreaId = userAddress.getDeliveryArea().getDeliveryAreaId();
    categoryService.findVerifiedCategoryById(categoryId);
    return storeRepository.findStoresByCategoryAndDeliveryArea(categoryId, deliveryAreaId, PageRequest.of(page, size));
  }

  /**
   * 가게 등록
   *
   * @Param categoryIds 카테고리 아이디
   * @Param deliveryAreas 배달 가능 지역
   * @Return Store
   */
  @Transactional
  public Store createStore(List<Long> categoryIds, List<String> deliveryAreas, Store store, long userId) {
    User user = userService.findUser(userId);
    store.setUser(user);
    verifyDuplicatedStoreInfo(store.getRegistrationNumber(), store.getTel(), store.getAddress(), store.getName());
    setCategoryStore(store, categoryIds);
    setDeliveryAreaStore(store, deliveryAreas);
    return storeRepository.save(store);
  }

  /**
   * Context Holder의 User와 가게의 사장님이 일치하는지 확인
   *
   * @throws BusinessLogicException when owner does not match.
   * @Param store 가게
   * @Param userId 사장님 아이디
   */
  public void verifyStoreOwner(Store store, long userId) {
    User storeOwner = store.getUser();
    if (userId != storeOwner.getUserId()) {
      throw new BusinessLogicException(ExceptionCode.OWNER_NOT_MATCHED);
    }
  }

  /**
   * storeId를 가지고 가게 사장님이 일치하는지 확인
   *
   * @param storeId
   * @param userId
   * @return
   */
  public Store verifyStoreOwner(Long storeId, long userId) {
    Store store = findStoreById(storeId);
    verifyStoreOwner(store, userId);
    return store;
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
   * 가게가 영업시간인지 확인
   *
   * @param store
   */
  @Transactional
  public void verifyOpen(Store store) {
    LocalTime now = LocalTime.now();
    LocalTime open = store.getOpenTime();
    LocalTime close = store.getCloseTime();
    if (!(open.isBefore(close) && now.isAfter(open) && now.isBefore(close)) ||
        !(open.isAfter(close) && (now.isAfter(open) || now.isBefore(close)))) {
      throw new BusinessLogicException(ExceptionCode.STORE_CLOSED);
    }
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
  @Transactional(readOnly = true)
  public Store findStoreById(long storeId) {
    Optional<Store> store = storeRepository.findById(storeId);
    return store.orElseThrow(() -> new BusinessLogicException(ExceptionCode.STORE_NOT_FOUND));
  }
}
