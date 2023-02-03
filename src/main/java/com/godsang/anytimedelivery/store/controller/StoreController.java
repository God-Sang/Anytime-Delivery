package com.godsang.anytimedelivery.store.controller;

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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 고객의 Store 검색을 위한 Controller
 */
@RestController
@RequestMapping("/customer/stores")
@RequiredArgsConstructor
public class StoreController {
  private final StoreService storeService;
  private final StoreMapper storeMapper;

  /**
   * CategoryId를 가지고 Store를 검색한다.
   * Pagination을 위해 page와 size 값도 입력 받는다.
   */
  @GetMapping
  public ResponseEntity findByCategory(@RequestBody @Valid StoreDto.GetRequest getDto) {
    Page<Store> stores = storeService.findByCategoryId(
        getDto.getCategoryId(), PageRequest.of(getDto.getPage(), getDto.getSize()));
    List<StoreDto.GetResponse> storeDtos = storeMapper.storeListToGetResponseDto(stores.getContent());
    return new ResponseEntity(new PageResponseDto(storeDtos, stores), HttpStatus.OK);
  }
}
