package com.example.pojazdy.controllers

import com.example.pojazdy.model.events.eventTypes.PartnerServiceEvents
import com.example.pojazdy.service.ServicePlanService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 *
 * @author Jakub Sapiński
 * */

@Slf4j
@RestController
@CompileStatic
@RequestMapping("/service_events")
class ServiceEventsController {

    private final ServicePlanService servicePlanService

    @Autowired
    ServiceEventsController(ServicePlanService servicePlanService) {
        this.servicePlanService = servicePlanService
    }

//    @GetMapping("/partner")
//    List<PartnerServiceEvents> servicePlans() {
//        servicePlanService.findServiceEventsForPartner()
//    }

}
