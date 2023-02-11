package com.godsang.anytimedelivery.store.controller;

import com.godsang.anytimedelivery.common.dto.SingleResponseDto;
import com.godsang.anytimedelivery.store.dto.StoreDto;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.mapper.StoreMapper;
import com.godsang.anytimedelivery.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/owner/stores")
@RequiredArgsConstructor
public class StoreForOwnerController {
  private final StoreService storeService;
  private final StoreMapper storeMapper;

  /**
   * 사장님이 가게를 등록한다.
   */
  @PostMapping
  public ResponseEntity postStore(@RequestBody @Valid StoreDto.Post postDto) {
    Store store = storeService.createStore(postDto.getCategoryIds(), postDto.getDeliveryAreas(), storeMapper.postDtoToStore(postDto));
    return new ResponseEntity(new SingleResponseDto<>(storeMapper.storeToResponseDto(store)), HttpStatus.CREATED);
  }
}
