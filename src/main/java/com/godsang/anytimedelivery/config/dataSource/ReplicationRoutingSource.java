package com.godsang.anytimedelivery.config.dataSource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Master 또는 Slave 중 사용할 data source를 routing
 * 현재 thread에서 사용할 data source는 determineCurrentLookupKey() 메서드에서 정한다.
 */
@Slf4j
public class ReplicationRoutingSource extends AbstractRoutingDataSource {
  private SlaveNames<String> slaveNames;

  /**
   * master, slave dataSource를 targetDataSource에 set한다.
   * slave의 dataSource명은 SlaveNames 필드로 생성한다.
   */
  @Override
  public void setTargetDataSources(Map<Object, Object> targetDataSources) {
    super.setTargetDataSources(targetDataSources);

    List<String> slaveNames = targetDataSources.keySet()
        .stream()
        .map(Object::toString)
        .filter(str -> str.contains(DataSourceType.SLAVE.toString()))
        .collect(Collectors.toList());

    this.slaveNames = new SlaveNames<>(slaveNames);
  }

  /**
   * @Transactional(readOnly = true) 트랜잭션은 slave 호출한다.
   * 이외의 트랜잭션은 master 호출한다.
   * return하는 data source명과 일치하는 targetDataSource를 사용한다.
   */
  @Override
  protected Object determineCurrentLookupKey() {
    boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

    if (isReadOnly) {
      String nextSlaveName = slaveNames.getNext();
      log.info("Slave connected: {}", nextSlaveName);
      return nextSlaveName;
    }
    log.info("Master connected");
    return DataSourceType.MASTER;
  }

  /**
   * 여러 개의 Slave 서버를 사용할 경우 부하를 분산한다.
   */
  private static class SlaveNames<T> {
    private final List<T> values;
    private int index = 0;

    private SlaveNames(List<T> values) {
      this.values = values;
    }

    private T getNext() {
      if (index >= values.size() - 1) {
        index = -1;
      }
      return values.get(++index);
    }

  }

}
