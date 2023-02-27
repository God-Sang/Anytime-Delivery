package com.godsang.anytimedelivery.address.service;

import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.service.DeliveryAreaService;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Save or update or retrieve an address.
 */
@Service
@RequiredArgsConstructor
public class AddressService {
  private final UserService userService;
  private final DeliveryAreaService deliveryAreaService;

  /**
   * save an address.
   * if there is an address already saved, then it is changed to the new one.
   * And, the old one is removed automatically.
   *
   * @param userId 사용자 아이디
   * @param address 사용자 주소
   * @param deliveryArea it consists of a combination of Jibun address components.(not Doromyung)
   */
  @Transactional
  @CacheEvict(cacheNames = "deliveryArea", key = "#userId")
  public void saveAddress(Long userId, Address address, String deliveryArea) {
    DeliveryArea foundDeliveryArea = deliveryAreaService.findExistedDeliveryArea(deliveryArea);
    address.setDeliveryArea(foundDeliveryArea);
    User user = userService.findUser(userId);
    user.setAddress(address);
  }

  /**
   * get an address of a user.
   *
   * @param userId 사용자 아이디
   * @return Address
   */
  @Transactional(readOnly = true)
  @Cacheable(cacheNames = "deliveryArea", key = "#userId")
  public Address getAddress(Long userId) {
    User user = userService.findUser(userId);
    Address address = user.getAddress();
    if (address == null) {
      throw new BusinessLogicException(ExceptionCode.ADDRESS_NOT_EXIST);
    }
    return address;
  }
}
