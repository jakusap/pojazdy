package com.example.pojazdy.service


import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import com.example.pojazdy.model.accounts.Account
import com.example.pojazdy.model.accounts.AuthRole
import com.example.pojazdy.repository.PartnersRepository

/**
 *
 * @author Jakub Sapiński
 * */

@Slf4j
@Component
@CompileStatic
class LoginService {

    private final PartnersRepository partnersRepository

    @Autowired
    LoginService(PartnersRepository partnersRepository) {
        this.partnersRepository = partnersRepository
    }

    String loginPartnerUUID() {
        partnersRepository.findPartnerByAcsId(loginAccount().acsId)?.uuid
    }

    Account loginAccount() {
        def loginAccount = ((KeycloakAuthenticationToken) SecurityContextHolder.context.authentication)
                .account.keycloakSecurityContext.token
        new Account(
                acsId: loginAccount.subject,
                email: loginAccount.email,
                referenceId: loginAccount.otherClaims["referenceId"] as String ?: null,
                authRole: parseRoleFromToken(loginAccount.realmAccess.roles)
        )
    }

    boolean userIsPartner() {
        return loginAccount().authRole == AuthRole.PARTNER
    }

    private static AuthRole parseRoleFromToken(Set<String> roles) {
        if(!roles){
            return AuthRole.NONE
        }
        if (roles.contains("partner")) {
            return AuthRole.PARTNER
        }
        return AuthRole.NONE
    }

}
