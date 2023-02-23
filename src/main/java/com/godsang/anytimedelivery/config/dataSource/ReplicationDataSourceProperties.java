package com.godsang.anytimedelivery.config.dataSource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * .properties 설정을 @ConfigurationProperties 어노테이션을 통해 객체로 매핑
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource")
public class ReplicationDataSourceProperties {
  private String driverClassName;
  private String username;
  private String password;
  private String url;
  private final Map<String, Slave> slaves = new HashMap<>();

  @Getter
  @Setter
  public static class Slave {
    private String name;
    private String driverClassName;
    private String username;
    private String password;
    private String url;
  }
}
