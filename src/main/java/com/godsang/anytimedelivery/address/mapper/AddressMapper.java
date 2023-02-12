package com.godsang.anytimedelivery.address.mapper;

import com.godsang.anytimedelivery.address.dto.AddressDto;
import com.godsang.anytimedelivery.address.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
  Address dtoToAddress(AddressDto dto);
  AddressDto addressToDto(Address address);
}