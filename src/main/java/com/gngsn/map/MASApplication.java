package com.gngsn.map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/secret.properties")
public class MASApplication {

    public static void main(String[] args) {
        SpringApplication.run(MASApplication.class, args);
    }
}
