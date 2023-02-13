package com.godsang.anytimedelivery.address.controller;

import com.godsang.anytimedelivery.address.dto.AddressDto;
import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.address.mapper.AddressMapper;
import com.godsang.anytimedelivery.address.service.AddressService;
import com.godsang.anytimedelivery.auth.utils.LoggedInUserInfoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/address")
public class AddressController {
  private final AddressService addressService;
  private final LoggedInUserInfoUtils loggedInUserInfoUtils;
  private final AddressMapper addressMapper;

  /** register an address to a user.*/
  @PostMapping
  public ResponseEntity registerAddress(@RequestBody @Valid AddressDto dto) {
    Long userId = loggedInUserInfoUtils.extractUserId();
    Address address = addressMapper.dtoToAddress(dto);
    addressService.saveAddress(userId, address, dto.getDeliveryArea());
    return new ResponseEntity(HttpStatus.OK);
  }
  /** retrieve an address of a user */
  @GetMapping
  public ResponseEntity getMyAddress() {
    Long userId = loggedInUserInfoUtils.extractUserId();
    Address address = addressService.getAddress(userId);
    AddressDto response = addressMapper.addressToDto(address);
    return new ResponseEntity(response, HttpStatus.OK);
  }
}
