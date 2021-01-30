package com.theshamuel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@ConstructorBinding
@ConfigurationProperties("dregupl")
@Validated
public class DreguplProps {

    private final String registrySourceLogin;
    private final String registrySourcePassword;
    private final String registryDestinationLogin;
    private final String registryDestinationPassword;

    public DreguplProps(String registrySourceLogin, String registrySourcePassword, String registryDestinationLogin, String registryDestinationPassword) {
        this.registrySourceLogin = registrySourceLogin;
        this.registrySourcePassword = registrySourcePassword;
        this.registryDestinationLogin = registryDestinationLogin;
        this.registryDestinationPassword = registryDestinationPassword;
    }

    public String getRegistrySourceLogin() {
        return registrySourceLogin;
    }

    public String getRegistrySourcePassword() {
        return registrySourcePassword;
    }

    public String getRegistryDestinationLogin() {
        return registryDestinationLogin;
    }

    public String getRegistryDestinationPassword() {
        return registryDestinationPassword;
    }
}
