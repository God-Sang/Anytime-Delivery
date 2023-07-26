package com.godsang.anytimedelivery.address.mapper;

import com.godsang.anytimedelivery.address.dto.AddressDto;
import com.godsang.anytimedelivery.address.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
  @Mapping(target = "deliveryArea", ignore = true)
  Address dtoToAddress(AddressDto dto);
  @Mapping(target = "deliveryArea", ignore = true)
  AddressDto addressToDto(Address address);
}
