## sk 루키즈 개발자 1기 미니프로젝트 2조

# 실행법 
1. docker-compose를 만들어서 vscode 아무데서나 열고, dokcer-compose up 하시면 이미지 깔림 
2. docker-desktop -> kafka run -> sns backend -> kafka backend -> react zzz 

# 주의 : 백엔드도 좀 바꿔놓아서, 그 프론트랑 카프카 서버만 뜯어가심 안됨니다.
예전에 Service에 AOP 걸어놔서 entity 뜯어와서, controller에서 AOP로 데이터 가져오는것 보다 성능 구림.
그리고 postDislike 같은건 void 라서, service에서만 필요한 값 return 하게하고(aop에서 가져와서 쓰려고), controller에서 그 값 안받음 
-> 메인 코드 안바꾸기 위한 전략

# 기능 관련 설명 이미지
![1-0](https://github.com/user-attachments/assets/b2294772-449a-4ae5-a59e-42030a55ec0e)
![1-1](https://github.com/user-attachments/assets/1aa300d6-0f93-48e7-871d-ca4fc309bffa)
![1-2](https://github.com/user-attachments/assets/45f424b2-19ab-477e-8f10-4d51683e5357)


# 카프카 알림 서비스 특징
1. 알림창 계속 열고있어도, 새 알림 오면 바로 추가됨
2. 로그아웃하고, 다시 재로그인하면, 안읽은 알람 다시보내주기 + 로그아웃 동안 발생한 이벤트 순서 맞춰 보내줌
3. 프론트 작업 좀 햇슴다. 그 모달 밖에 누르면 닫히게 만듬. 


# 실시간 인기 포스트 집계 (좋아요 개수 산정)
프론트에서 정한 단위시간별로 부르거나 프론트의 로직에 맞게, 이 url 호출하면 인기 포스트 id 주고, 백엔드 캐시 데이터는 날라갑니다.


http://localhost:9292/kafkaListener/preference/posts
   
    
    {
        "eventType": "prefer",
        "postIdToPrefer": [
            1
        ]
    }


### kafka image docker-compose
    version: '3'
    
    services:
      zookeeper:
        image: wurstmeister/zookeeper
        container_name: zookeeper
        ports:
          - "2181:2181"
      kafka:
        image: wurstmeister/kafka
        container_name: kafka
        ports:
          - "9092:9092"
        environment:
          KAFKA_ADVERTISED_HOST_NAME: localhost
          KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181


### kakfaConsumer application.properties
    server.port=9292
    
    spring.application.name=kafka-consumer
    
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.url=jdbc:mysql://localhost:3306/KafkaConsumerDB?createDatabaseIfNotExist=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
    spring.datasource.username={}
    spring.datasource.password={}
    spring.jpa.hibernate.ddl-auto=update
    
    
    notifications.bootstrap-servers=localhost:9092
    notifications.group-id-1=test-group-notify
    notifications.group-id-2=test-group-dataAgg
    
    notifications.topic-name=test-topic-notify
    
    notifications.max_poll_interval_ms=1800000


### sns main 
        spring.application.name=delta
        spring.datasource.driver-class-name =com.mysql.cj.jdbc.Driver
        spring.datasource.url=jdbc:mysql://localhost:3306/DeltaDB2?createDatabaseIfNotExist=true
        spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
        spring.datasource.username=root
        spring.datasource.password=p@ssw0rd
        spring.jpa.hibernate.ddl-auto=update
        
        token.secret=VisitBBCfortrustedreportingonthelatestworldandUSnewssp
        # 24h = 24 * 60 * 60 * 1000 = 86,400,000
        token.expiration-time=86400000
        
        spring.mvc.static-path-pattern=/uploads/**
        spring.web.resources.static-locations=classpath:/static/uploads/
        
        server.port=8080
        notifications.bootstrap-servers=localhost:9092
        notifications.topic-name=test-topic-notify


### 실행법 
1. docker-compose를 vscode 아무데서나 열고, dokcer-compose up 하시면 이미지 깔림 
2. docker-desktop -> kafka run -> sns backend -> kafka backend -> react zzz 

