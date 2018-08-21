package com.mozdzo.keycloak.setup

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@Component
@ConfigurationProperties(prefix = 'test.keycloak')
class TestKeycloakConfiguration {

    @Min(1L)
    int port

    @NotBlank
    String username

    @NotBlank
    String password

    @NotBlank
    String realmJsonPath
}
