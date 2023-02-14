package com.godsang.anytimedelivery.store.controller;

import com.godsang.anytimedelivery.auth.utils.LoggedInUserInfoUtils;
import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import com.godsang.anytimedelivery.common.dto.PageResponseDto;
import com.godsang.anytimedelivery.store.dto.StoreDto;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.mapper.StoreMapper;
import com.godsang.anytimedelivery.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 고객의 Store 검색을 위한 Controller
 */
@RestController
@RequestMapping("/categories/{category-id}/stores")
@RequiredArgsConstructor
@Validated
public class StoreForCustomerController {
  private final StoreService storeService;
  private final StoreMapper storeMapper;
  private final LoggedInUserInfoUtils loggedInUserInfoUtils;

  /**
   * Search stores based on a category and address of a user.
   *
   * @param categoryId
   * @param page
   * @param size
   * @return ResponseEntity
   */
  @GetMapping
  public ResponseEntity getStores(@PathVariable("category-id") @Positive long categoryId,
                                  @RequestParam @Positive int page,
                                  @RequestParam @Positive int size) {
    Long userId = loggedInUserInfoUtils.extractUserId();
    if (userId == null) {
      throw new BusinessLogicException(ExceptionCode.LOG_IN_REQUIRED);
    }
    Page<Store> stores = storeService.findStoresByCategoryId(categoryId, userId, PageRequest.of(page, size));
    List<StoreDto.Response> storeDtos = storeMapper.storeListToResponseDto(stores.getContent());
    return new ResponseEntity(new PageResponseDto<>(storeDtos, stores), HttpStatus.OK);
  }
}
