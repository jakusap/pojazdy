package com.example.pojazdy.controllers

import com.example.pojazdy.model.ServicePlan
import com.example.pojazdy.model.annotations.HasPartnerRole
import com.example.pojazdy.model.events.ServiceEvent
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

    @HasPartnerRole
    @PostMapping("insertEvent/{servicePlanId}")
    void addServiceEvent(@PathVariable("servicePlanId") int servicePlanId, @RequestBody ServiceEvent serviceEvent) {
        serviceEvent.planId = servicePlanId
        servicePlanService.addServiceEvent(serviceEvent)
    }

    @HasPartnerRole
    @PutMapping("/updateEvent/{orderNumber}")
    void updateServicePlan(@PathVariable("orderNumber") int orderNumber, @RequestBody ServiceEvent serviceEvent) {
        servicePlanService.updateServiceEvent(serviceEvent)
    }

}
