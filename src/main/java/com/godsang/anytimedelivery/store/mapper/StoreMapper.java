package com.godsang.anytimedelivery.store.mapper;

import com.godsang.anytimedelivery.store.dto.StoreDto;
import com.godsang.anytimedelivery.store.entity.Store;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreMapper {
  StoreDto.Response storeToResponseDto(Store store);

  List<StoreDto.Response> storeListToGetResponseDto(List<Store> stores);

  Store postDtoToStore(StoreDto.Post postDto);
}
