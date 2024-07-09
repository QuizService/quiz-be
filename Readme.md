## 선착순 퀴즈 서비스(Backend)

## Overview
사용자가 직접 퀴즈를 만들고 참여코드를 통해 선착순으로 퀴즈에 참여할 수 있는 서비스입니다. 

### Project Architecture

* OpenJDK 21
* SpringBoot 3.2.6 (Servlet MVC), Spring Data JPA
* MySQL, MongoDB
* Redis for Redisson(Distributed Lock)
* WebSocket and Redis Sorted Set for Waiting Queue
* Stateless Session Management with JWT + Spring Security + Google OAuth2
* Module Architecture with Gradle Multi-Project

### Module
```
- 🗂️ api-module
- 🗂️ domain-quiz-module
- 🗂️ global-util-module
```

### Github Actions CI/CD



### Project Outline

### 담당한 기능

 * Spring Boot 기반 RestAPI 구현
 * 다양한 퀴즈 유형을 위한 MongoDB 도입
   * [MongoDB Replication을 활용한 트랜잭션이 적용되지 않는 이슈 해결](https://velog.io/@penrose_15/Docker-MongoDB-replicaSet-설정)
 * [분산락(Redisson)을 활용하여 퀴즈 선착순 참여시 발생할 수 있는 동시성 이슈 해결](https://velog.io/@penrose_15/Redisson을-활용한-분산락으로-동시성-이슈-해결하기)
 * [Redis Sorted Set + WebSocket를 활용한 대기열 구현으로 트래픽 급증 대비](https://velog.io/@penrose_15/Redis-Websocket-활용한-대기열-서비스-구현Spring-Boot)
 * [Google OAuth2 적용](https://velog.io/@penrose_15/SpringBoot-React-환경에서-Google-Oauth2-적용기)
 * Refresh Token Rotation기법으로 Refresh Token 로 토큰 탈취 대응
 * Github actions + AWS S3 + AWS CodeDeploy기반 Blue/Green 배포 구현
