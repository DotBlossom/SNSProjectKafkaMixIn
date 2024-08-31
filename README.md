## 실행법 
1. docker-compose를 vscode 아무데서나 열고, dokcer-compose up 하시면 이미지 깔림 
2. docker-desktop -> kafka run -> sns backend -> kafka backend -> react zzz 

## 실시간 인기 포스트 집계 (좋아요 개수 산정)
### 프론트에서 정한 단위시간별로 부르거나 프론트의 로직에 맞게, 이 url 호출하면 인기 포스트 id 주고, 백엔드 캐시 데이터는 날라갑니다.
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

