package com.theshamuel;

import com.theshamuel.config.DreguplProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(DreguplProps.class)
@SpringBootApplication
public class BootstrapApp {
    public static void main(String[] args) {
        SpringApplication.run(BootstrapApp.class, args);
    }
}
