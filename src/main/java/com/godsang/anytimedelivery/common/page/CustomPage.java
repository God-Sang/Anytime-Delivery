package com.godsang.anytimedelivery.common.page;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class CustomPage<T> extends PageImpl<T> {
  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public CustomPage(@JsonProperty("content") List<T> content,
                    @JsonProperty("size") int size,
                    @JsonProperty("number") int page,
                    @JsonProperty("totalElements") long total) {
    super(content, PageRequest.of(page, size), total);
  }

  public CustomPage(List<T> content) {
    super(content);
  }
}
