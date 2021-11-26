package com.example.pojazdy.service.auth

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.example.pojazdy.exceptions.PojazdyException

@Slf4j
@Service
@CompileStatic
@Transactional
class KeycloakAuthService implements ProviderAuthService {

    private final Keycloak keycloak
    private final String realm

    @Autowired
    KeycloakAuthService(@Value("\${keycloak.auth-server-url}") String serverUrl,
                        @Value("\${keycloak.realm}") String realm,
                        @Value("\${keycloak-admin-client.id}") String clientId,
                        @Value("\${keycloak-admin-client.user}") String user,
                        @Value("\${keycloak-admin-client.pass}") String pass) {
        this.realm = realm

        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .username(user)
                .password(pass)
                .build()
        log.info("Keycloak: {}", keycloak)
    }

    void deleteUser(String userId) {
        def response = keycloak.realm(realm).users().delete(userId)
        if (response.status != 204) {
            log.error("Unable to delete keycloak account. Reason: {}", response.statusInfo.getReasonPhrase())
            throw new PojazdyException("Unable to delete keycloak account")
        }
    }
}
