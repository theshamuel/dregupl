package com.theshamuel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@ConstructorBinding
@ConfigurationProperties("dregupl")
@Validated
public class DreguplProps {

    private final String registrySourceURL;
    private final String registrySourceLogin;
    private final String registrySourcePassword;
    private final String registryDestinationURL;
    private final String registryDestinationLogin;
    private final String registryDestinationPassword;

    public DreguplProps(String registrySourceURL, String registrySourceLogin, String registrySourcePassword,
                        String registryDestinationURL, String registryDestinationLogin,
                        String registryDestinationPassword) {
        this.registrySourceURL = registrySourceURL;
        this.registrySourceLogin = registrySourceLogin;
        this.registrySourcePassword = registrySourcePassword;
        this.registryDestinationURL = registryDestinationURL;
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

    public String getRegistrySourceURL() {
        return registrySourceURL;
    }

    public String getRegistryDestinationURL() {
        return registryDestinationURL;
    }
}
