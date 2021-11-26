package com.example.pojazdy.controllers

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.http.ResponseEntity

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.example.pojazdy.model.annotations.HasPartnerRole

/**
 *
 * @author Jakub Sapiński
 * */
@Slf4j
@RestController
@CompileStatic
@RequestMapping("/login")
class LoginController {

    @HasPartnerRole
    @PostMapping
    ResponseEntity login() {
        log.info("Status to: {}", ResponseEntity);
        log.info("User {} logged in.", SecurityContextHolder.context.authentication.name)

        ResponseEntity.ok().build()
    }
}
