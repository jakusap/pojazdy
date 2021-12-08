package com.example.pojazdy.controllers

import com.example.pojazdy.model.annotations.HasPartnerRole
import com.example.pojazdy.model.events.eventTypes.PartnerServiceEventList
import com.example.pojazdy.repository.*
import com.example.pojazdy.service.LoginService
import com.example.pojazdy.service.ServicePlanService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import groovy.util.logging.Slf4j

/**
 *
 * @author Jakub Sapiński
 * */
@Slf4j
@RestController
@RequestMapping('/message')
class MessageController {

    private final PartnersRepository partnersRepository
    private final LoginService loginService
    private final ServicePlanService servicePlanService

    @Autowired
    MessageController(PartnersRepository partnersRepository, LoginService loginService, ServicePlanService servicePlanService) {
        this.partnersRepository = partnersRepository
        this.loginService = loginService
        this.servicePlanService = servicePlanService
    }

    @HasPartnerRole
    @GetMapping
    String mainPage() {
        return "It is a main page"
    }

    @HasPartnerRole
    @GetMapping("/partnerServiceEvents")
    List<PartnerServiceEventList> partnerServiceEvents() {
        servicePlanService.findServiceEventsForPartner()
    }

    @HasPartnerRole
    @PutMapping("/updatePartnerServiceEvents/{servicePlanId}/{orderNumber}")
    void updatePartnerServiceEvents(@PathVariable("servicePlanId") int servicePlanId, @PathVariable("orderNumber") int orderNumber) {
        servicePlanService.updateServiceEventsForPartner(servicePlanId, orderNumber)
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
