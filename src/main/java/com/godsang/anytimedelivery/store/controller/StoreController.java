package com.godsang.anytimedelivery.store.controller;

import com.godsang.anytimedelivery.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/stores")
@RequiredArgsConstructor
public class StoreController {
  private static StoreService storeService;
}
