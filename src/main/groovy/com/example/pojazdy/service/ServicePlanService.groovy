package com.example.pojazdy.service

import com.example.pojazdy.exceptions.BadRequestException
import com.example.pojazdy.model.ServicePlan
import com.example.pojazdy.model.cars.Car
import com.example.pojazdy.model.events.eventTypes.PartnerServiceEventList
import com.example.pojazdy.model.events.eventTypes.PartnerServiceEvents

import com.example.pojazdy.repository.ServicePlanRepository
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.util.stream.Collectors
import java.util.stream.Stream

/**
 *
 * @author Jakub Sapiński
 * */

@Slf4j
@Service
@CompileStatic
class ServicePlanService {

    private final ServicePlanRepository servicePlanRepository
    private final LoginService loginService

    @Autowired
    ServicePlanService(ServicePlanRepository servicePlanRepository, LoginService loginService) {
        this.servicePlanRepository = servicePlanRepository
        this.loginService = loginService
    }

    void addServicePlan(ServicePlan servicePlan) {
        def partnerUUID = loginService.loginPartnerUUID()
        if (!servicePlan.isValid()) {
            throw new BadRequestException("ServicePlan is invalid!")
        }
        servicePlanRepository.insert(partnerUUID, servicePlan)
    }

    void updateServicePlan(ServicePlan servicePlan) {
        def partnerUUID = loginService.loginPartnerUUID()
        servicePlanRepository.updateServicePlan(partnerUUID, servicePlan)
    }

    ServicePlan findSpecificServicePlanDetails(int servicePlanId)
    {
        def partnerUUID = loginService.loginPartnerUUID()
        def servicePlan = servicePlanRepository.findSpecificServicePlanDetails(partnerUUID, servicePlanId)
        servicePlan.serviceEvents = servicePlanRepository.findServiceEventsForSpecificPlan(servicePlan.servicePlanId)
        servicePlan
    }

    List<ServicePlan> findServicePlansForPartner() {
        def partnerUUID = loginService.loginPartnerUUID()
        servicePlanRepository.findServicePlansForPartner(partnerUUID)
    }

    ServicePlan findServicePlansForSpecificCar(int carId) {
        def partnerUUID = loginService.loginPartnerUUID()
        def servicePlan = servicePlanRepository.findServicePlansForSpecificCar(partnerUUID, carId)
        if (servicePlan) {
            servicePlan.serviceEvents = servicePlanRepository.findServiceEventsForSpecificPlan(servicePlan.servicePlanId)
        }

        servicePlan
    }


    List<PartnerServiceEventList> findServiceEventsForPartner() {
        def partnerUUID = loginService.loginPartnerUUID()
        def partnerServicePlanEvents = servicePlanRepository.findPartnerServicePlanEvents(partnerUUID)
        partnerServicePlanEvents
    }

}
