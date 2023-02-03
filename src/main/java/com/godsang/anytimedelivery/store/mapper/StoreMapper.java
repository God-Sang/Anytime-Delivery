package com.godsang.anytimedelivery.store.mapper;

import com.godsang.anytimedelivery.store.dto.StoreDto;
import com.godsang.anytimedelivery.store.entity.Store;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreMapper {
  StoreDto.GetResponse storeToResponseDto(Store store);

  List<StoreDto.GetResponse> storeListToGetResponseDto(List<Store> stores);
}
