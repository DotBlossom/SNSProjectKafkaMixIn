# SNS 프로젝트 backend 파트

## application.properties 데이터베이스 구현 코드
```commandline
spring.application.name=delta
spring.datasource.driver-class-name =com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/DeltaDB?createDatabaseIfNotExist=true
spring.datasource.username={id}
spring.datasource.password={password}
spring.jpa.hibernate.ddl-auto=update

token.secret={토큰 secret}
# 24h = 24 * 60 * 60 * 1000 = 86,400,000
token.expiration-time=86400000

spring.mvc.static-path-pattern=/uploads/**
spring.web.resources.static-locations=classpath:/static/uploads/
```
