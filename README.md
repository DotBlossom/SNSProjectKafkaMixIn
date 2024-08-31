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



