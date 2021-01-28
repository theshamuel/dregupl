package com.theshamuel;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate restTemplateRegTo(RestTemplateBuilder builder) {
        RestTemplate r =  builder.basicAuthentication("admin2", "admin2", StandardCharsets.UTF_8).build();

//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(10);
//        requestFactory.setReadTimeout(10);
//
//        r.setRequestFactory(requestFactory);
        return r;
    }

    @Bean
    public RestTemplate restTemplateRegFrom(RestTemplateBuilder builder) {
        return builder.basicAuthentication("admin", "admin", StandardCharsets.UTF_8).build();
    }
}
