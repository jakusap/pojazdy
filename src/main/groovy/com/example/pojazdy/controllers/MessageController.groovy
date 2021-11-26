package com.example.pojazdy.controllers

import com.example.pojazdy.model.annotations.HasPartnerRole
import com.example.pojazdy.repository.*
import com.example.pojazdy.service.LoginService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import groovy.util.logging.Slf4j

/**
 *
 * @author Jakub Sapiński
 * */
@Slf4j
@RestController
@RequestMapping('/')
class MessageController {

    private final PartnersRepository partnersRepository
    private final LoginService loginService

    @Autowired
    MessageController(PartnersRepository partnersRepository, LoginService loginService) {
        this.partnersRepository = partnersRepository
        this.loginService = loginService
    }

    @GetMapping
    String mainPage() {
        return "It is a main page"
    }

    @HasPartnerRole
    @GetMapping('/welcome')
    String welcomePage() {
        log.info("Token {}", SecurityContextHolder.context.authentication)
        def login = loginService.loginPartnerUUID()
        log.info("Login: {}", login)
        return "Welcome"
    }
}
