package com.example.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class RestApiApplication {

    //earlier the link was http, which was not secure and retrofit was giving
    //handshake error in android
    //so we made the link https, using the below article
    //https://medium.com/quick-code/spring-boot-how-to-secure-rest-api-with-https-54ec8f0e4796

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }

}
