package com.godsang.anytimedelivery.category.controller;

import com.godsang.anytimedelivery.category.dto.CategoryDto;
import com.godsang.anytimedelivery.category.service.CategoryService;
import com.godsang.anytimedelivery.common.dto.MultiResponseDto;
import com.godsang.anytimedelivery.common.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity getAllCategories() {
    return new ResponseEntity(
        new MultiResponseDto(categoryService.getAllCategories()), HttpStatus.OK
    );
  }

  @PatchMapping
  public ResponseEntity updateCategory(@RequestBody CategoryDto.Patch patchDto) {
    return new ResponseEntity(
        new SingleResponseDto(categoryService.updateCategory(patchDto.getPreName(), patchDto.getCurName())), HttpStatus.OK
    );
  }

  @PostMapping
  public ResponseEntity addCategory(@RequestBody CategoryDto.Post postDto) {
    return new ResponseEntity(
        new SingleResponseDto(categoryService.addCategory(postDto.getName())), HttpStatus.CREATED
    );
  }

  @DeleteMapping
  public ResponseEntity deleteCategory(@RequestParam String name) {
    categoryService.deleteCategory(name);
    return new ResponseEntity(HttpStatus.OK);
  }
}
