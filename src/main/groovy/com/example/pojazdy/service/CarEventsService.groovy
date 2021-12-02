package com.example.pojazdy.service

import com.example.pojazdy.model.ServicePlan
import com.example.pojazdy.model.cars.Car
import com.example.pojazdy.model.events.CarEvent
import com.example.pojazdy.repository.CarEventsRepository
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 *
 * @author Jakub Sapiński
 * */
@Slf4j
@Service
@CompileStatic
class CarEventsService {
    private final CarEventsRepository carEventsRepository
    private final LoginService loginService

    @Autowired
    CarEventsService(CarEventsRepository carEventsRepository, LoginService loginService) {
        this.carEventsRepository = carEventsRepository
        this.loginService = loginService

    }

    void addCarEvent(CarEvent carEvent) {
        def partnerUUID = loginService.loginPartnerUUID()
        carEventsRepository.insert(partnerUUID, carEvent)
    }

    void updateCarEvent(CarEvent carEvent) {
        def partnerUUID = loginService.loginPartnerUUID()
        carEventsRepository.updateCarEvent(partnerUUID, carEvent)
    }

    List<CarEvent> findCarEventsForPartnerCar(int carId) {
        def partnerUUID = loginService.loginPartnerUUID()
        carEventsRepository.findCarEventsForPartnersCar(partnerUUID, carId)
    }
}


