package com.godsang.anytimedelivery.dataSource;

import com.godsang.anytimedelivery.config.dataSource.DataSourceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class DataSourceTest {
  public final String TEST_METHOD = "determineCurrentLookupKey";

  @Test
  @DisplayName("Master Data Source 분기 테스트")
  @Transactional
  void masterDataSourceTest(@Qualifier("routingDataSource") DataSource routingDataSource) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method determineCurrentLookupKey = AbstractRoutingDataSource.class.getDeclaredMethod(TEST_METHOD);
    determineCurrentLookupKey.setAccessible(true);

    String dataSourceType = (String) determineCurrentLookupKey.invoke(routingDataSource);

    assertThat(dataSourceType).isEqualTo(DataSourceType.MASTER.getName());
  }

  @Test
  @DisplayName("Slave Data Source 분기 테스트")
  @Transactional(readOnly = true)
  void slaveDataSourceTest(@Qualifier("routingDataSource") DataSource routingDataSource) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method determineCurrentLookupKey = AbstractRoutingDataSource.class.getDeclaredMethod(TEST_METHOD);
    determineCurrentLookupKey.setAccessible(true);

    String dataSourceType = (String) determineCurrentLookupKey.invoke(routingDataSource);

    assertThat(dataSourceType).contains(DataSourceType.SLAVE.getName());
  }
}
