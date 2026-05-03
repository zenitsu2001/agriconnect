package com.agriconnect.agriconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
//@EnableDiscoveryClient   // Enregistrement auprès d'Eureka
@EnableKafka             // Activation des consumers Kafka
@EnableJpaAuditing       // Support de @CreatedDate / @LastModifiedDate
public class AgriconnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgriconnectApplication.class, args);
    }

}
