package com.example.pojazdy.controllers

import com.example.pojazdy.model.ServicePlan
import com.example.pojazdy.model.annotations.HasPartnerRole
import com.example.pojazdy.model.cars.Car
import com.example.pojazdy.model.events.ServiceEvent
import com.example.pojazdy.model.events.eventTypes.PartnerServiceEventList
import com.example.pojazdy.model.events.eventTypes.PartnerServiceEvents
import com.example.pojazdy.service.CarsService
import com.example.pojazdy.service.ServicePlanService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.bouncycastle.cert.ocsp.Req
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 *
 * @author Jakub Sapiński
 * */

@Slf4j
@RestController
@CompileStatic
@RequestMapping("/service_plans")
class ServicePlansController {
    private final ServicePlanService servicePlanService

    @Autowired
    ServicePlansController(ServicePlanService servicePlanService) {
        this.servicePlanService = servicePlanService
    }

    @HasPartnerRole
    @GetMapping
    List<ServicePlan> servicePlans() {
        servicePlanService.findServicePlansForPartner()
    }

    @HasPartnerRole
    @GetMapping("/specificServicePlanDetails/{servicePlanId}")
    ServicePlan getSpecificServicePlanDetails(@PathVariable("servicePlanId") int servicePlanId){
        servicePlanService.findSpecificServicePlanDetails(servicePlanId)
    }

    @HasPartnerRole
    @GetMapping("/partnerServiceEvents")
    List<PartnerServiceEventList> partnerServiceEvents() {
        servicePlanService.findServiceEventsForPartner()
    }

    @HasPartnerRole
    @PostMapping
    void addServicePlan(@RequestBody ServicePlan servicePlan) {
        servicePlanService.addServicePlan(servicePlan)
    }

    @HasPartnerRole
    @PutMapping("/update/{servicePlanId}")
    void updateServicePlan(@PathVariable("servicePlanId") int servicePlanId, @RequestBody ServicePlan servicePlan) {
        servicePlan.servicePlanId = servicePlanId
        servicePlanService.updateServicePlan(servicePlan)
    }

    @HasPartnerRole
    @GetMapping("/specificServicePlan/{carId}")
    ServicePlan findSpecificServicePlanForCar(@PathVariable("carId") String carId) {
        servicePlanService.findServicePlansForSpecificCar(carId as int)
    }
}
