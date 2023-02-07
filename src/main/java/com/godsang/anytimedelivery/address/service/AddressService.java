package com.godsang.anytimedelivery.address.service;

import com.godsang.anytimedelivery.address.dto.AddressDto;
import com.godsang.anytimedelivery.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
@RequiredArgsConstructor
public class AddressService {
  private static AddressRepository addressRepository;
  private final String URI = "https://business.juso.go.kr/addrlink/addrLinkApi.do";
  @Value("${address.search.key}")
  private String key;

  public AddressDto.SearchResponseDto searchAddress(String keyword, int page, int size) {
    //인코딩 기능을 추가한 URI 빌더
    DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(URI);
    factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);

    //WebClient 인스턴스 생성
    WebClient client = WebClient.builder()
        .uriBuilderFactory(factory)
        .baseUrl(URI)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .build();

    AddressDto.SearchResponseDto response = client.get()
        .uri(uriBuilder -> uriBuilder
            .queryParam("confmKey", key)
            .queryParam("currentPage", page)
            .queryParam("countPerPage", size)
            .queryParam("keyword", keyword)
            .queryParam("resultType", "json")
            .build()
        )
        .retrieve()
        .bodyToMono(AddressDto.SearchResponseDto.class)
        .block();
    return response;
  }
}
