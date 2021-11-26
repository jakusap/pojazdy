package com.example.pojazdy.configuration

import org.keycloak.adapters.KeycloakDeployment
import org.keycloak.adapters.KeycloakDeploymentBuilder
import org.keycloak.adapters.spi.HttpFacade
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties
import org.springframework.context.annotation.Configuration

/**
 *
 * @author Jakub Sapiński
 * */
@Configuration
class KeycloakConfigResolver extends KeycloakSpringBootConfigResolver {

    private final KeycloakDeployment keycloakDeployment

    KeycloakConfigResolver(KeycloakSpringBootProperties properties) {
        keycloakDeployment = KeycloakDeploymentBuilder.build(properties)
    }

    @Override
    KeycloakDeployment resolve(HttpFacade.Request facade) {
        keycloakDeployment
    }

}
