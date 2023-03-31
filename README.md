# Anytime-Delivery (진행 중)
* 배달의 민족을 모티브한 배달 플랫폼 API 서버입니다.
* 도메인과 관련된 기능 뿐 아니라 대용량 트래픽을 처리할 상황도 고려하여 구현했습니다.
* 성능 개선에 있어 다양한 이슈를 경험하고 해결하는 것이 목표입니다.
* 재사용, 유지보수가 용이한 코드를 작성하려고 노력했고, 단위 테스트에 높은 우선순위를 두고 프로젝트를 진행했습니다.

## Architecture
![architecture](https://user-images.githubusercontent.com/101033262/229098013-904ebb51-59cf-41cf-950b-80379f62bb65.png)

## 프로젝트 주요 관심 사항
#### 브랜치 관리
* Git Flow 전략 채택
  * main: 배포 단계에서 사용하는 브랜치
  * dev: 기능을 병합하는 브랜치
  * feature: 기능 개발을 진행하는 브랜치
  * hotfix: 배포 후 발생한 버그를 수정하는 브랜치
  
* reference: [우아한 형제들 기술 블로그](https://techblog.woowahan.com/2553/)

#### 코드 관련
* [Google Code Style](https://google.github.io/styleguide/javaguide.html) 준수
* Javadoc을 사용하여 기능 문서화
* 클린 코드를 작성하기 위해 지속적인 리팩토링

#### 성능 최적화
* 서버 부하를 줄이기 위해 캐싱 활용
* MySQL Replication을 적용하여 Master-Slave 데이터베이스 이중화
* Nginx의 reversed-proxy를 사용하여 로드밸런싱
* Thread Pool을 설정하고 @Async 어노테이션을 사용하여 비동기 푸쉬 알림 구현
* MySQL 실행 계획 분석 후 인덱스, 쿼리 튜닝

#### 테스트
* Junit5를 기반으로 단위 테스트 코드 작성
* Github Actions 테스트 자동화
* nGrinder를 설치하여 부하 테스트 진행
* Jacoco 라이브러리를 적용하여 코드 커버리지 측정

#### CI/CD
* Github Actions를 사용하여 CI/CD 환경 구축
* Docker 이미지를 제작하여 배포

## Issue Posting
1. [MySQL Replication을 구축한 과정](https://velog.io/@given53/Spring-Docker-MySQL-master-slave-Replication-Spring-Data-JPA)

2. session 기반 Spring Security와 분산 서버 환경에서 session 불일치 해결

3. Security Context의 Authentication에 접근하는 메서드 테스트

4. Redis 캐시 저장소와 세션 저장소 분리하기

5. JPA N+1 문제 해결하기

6. Cache를 적용하여 조회 성능 향상시키기

7. 실행 계획 분석 후 SQL 성능 튜닝

7. 비동기 방식으로 푸쉬 알림 전송

8. nGrinder 부하 테스트

9. Github Actions에서 CI/CD 자동화를 구축한 과정

10. Jacoco 코드 커버리지 n% 달성


## ERD
![erd](https://user-images.githubusercontent.com/101033262/228627922-fdf14861-9a64-4c3d-94a8-3d56f3e10898.png)

## Prototype
https://github.com/God-Sang/Anytime-Delivery/wiki/%ED%99%94%EB%A9%B4-%EC%84%A4%EA%B3%84
