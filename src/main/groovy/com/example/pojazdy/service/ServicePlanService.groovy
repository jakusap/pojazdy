package com.example.pojazdy.service

import com.example.pojazdy.exceptions.BadRequestException
import com.example.pojazdy.model.ServicePlan
import com.example.pojazdy.model.cars.Car
import com.example.pojazdy.model.events.ServiceEvent
import com.example.pojazdy.model.events.eventTypes.PartnerServiceEventList
import com.example.pojazdy.model.events.eventTypes.PartnerServiceEvents
import com.example.pojazdy.repository.CarsRepository
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
    private final CarsRepository carsRepository

    @Autowired
    ServicePlanService(ServicePlanRepository servicePlanRepository, LoginService loginService, CarsRepository carsRepository) {
        this.servicePlanRepository = servicePlanRepository
        this.loginService = loginService
        this.carsRepository = carsRepository
    }

    void addServicePlan(ServicePlan servicePlan) {
        def partnerUUID = loginService.loginPartnerUUID()
        if (!servicePlan.isValid()) {
            throw new BadRequestException("ServicePlan is invalid!")
        }
        servicePlanRepository.insert(partnerUUID, servicePlan)
    }

    void addServiceEvent(ServiceEvent serviceEvent) {
        def partnerUUID = loginService.loginPartnerUUID()
        servicePlanRepository.insertServiceEvent(partnerUUID, serviceEvent)
    }

    void updateServicePlan(ServicePlan servicePlan) {
        def partnerUUID = loginService.loginPartnerUUID()
        servicePlanRepository.updateServicePlan(partnerUUID, servicePlan)
    }

    void updateServiceEvent(ServiceEvent serviceEvent) {
        def partnerUUID = loginService.loginPartnerUUID()
        servicePlanRepository.updateServiceEvent(partnerUUID, serviceEvent)
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


    void updateServiceEventsForPartner(int servicePlanId, int orderNumber) {
        if(servicePlanId > 0 && orderNumber > 0) {
            servicePlanRepository.removeServiceEvent(servicePlanId, orderNumber)
        }
        else {
            throw new BadRequestException("ServiceEvent is invalid!")
        }
    }

    List<PartnerServiceEventList> findServiceEventsForPartner() {
        def partnerUUID = loginService.loginPartnerUUID()
        def events = new ArrayList<PartnerServiceEventList>()
        def cars = carsRepository.findCarsForPartner(partnerUUID)
        cars.each {
            if (it.servicePlanId > 0) {
                def event = new PartnerServiceEventList()
                event.carId = it.carId
                event.carMake = it.carMake
                event.carModel = it.carModel
                event.registrationNumber = it.registrationNumber
                event.serviceEvents = servicePlanRepository.findPartnerServicePlanEvents(partnerUUID, it.carId)
                events.add(event)
            }
        }
        events
    }

}
