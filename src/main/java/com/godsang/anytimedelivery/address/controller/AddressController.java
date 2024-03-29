package com.godsang.anytimedelivery.address.controller;

import com.godsang.anytimedelivery.address.dto.AddressDto;
import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.address.mapper.AddressMapper;
import com.godsang.anytimedelivery.address.service.AddressService;
import com.godsang.anytimedelivery.auth.details.UserDetailsImpl;
import com.godsang.anytimedelivery.common.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/address")
public class AddressController {
  private final AddressService addressService;
  private final AddressMapper addressMapper;

  /**
   * register an address to a user.
   */
  @PostMapping
  public ResponseEntity registerAddress(@RequestBody @Valid AddressDto dto,
                                        @AuthenticationPrincipal UserDetailsImpl principal) {
    Long userId = principal.getUserId();
    Address address = addressMapper.dtoToAddress(dto);
    addressService.saveAddress(userId, address, dto.getDeliveryArea());
    return new ResponseEntity(HttpStatus.OK);
  }

  /**
   * retrieve an address of a user
   */
  @GetMapping
  public ResponseEntity getMyAddress(@AuthenticationPrincipal UserDetailsImpl principal) {
    Long userId = principal.getUserId();
    Address address = addressService.getAddress(userId);
    AddressDto response = addressMapper.addressToDto(address);
    return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
  }
}
