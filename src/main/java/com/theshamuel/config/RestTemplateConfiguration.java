package com.theshamuel.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class RestTemplateConfiguration {

    private final DreguplProps props;

    public RestTemplateConfiguration(DreguplProps props) {
        this.props = props;
    }

    @Bean
    public RestTemplate restTemplateRegTo(RestTemplateBuilder builder) {
        return builder.basicAuthentication(props.getRegistryDestinationLogin(), props.getRegistryDestinationPassword(), StandardCharsets.UTF_8).build();
    }

    @Bean
    public RestTemplate restTemplateRegFrom(RestTemplateBuilder builder) {
        return builder.basicAuthentication(props.getRegistrySourceLogin(), props.getRegistrySourcePassword(), StandardCharsets.UTF_8).build();
    }
}
