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

    @Autowired
    CarEventsService(CarEventsRepository carEventsRepository) {
        this.carEventsRepository = carEventsRepository
    }

    void addCarEvent(CarEvent carEvent) {
        def partnerUUID = '2169d3f0-98ba-4f73-8f92-015effd24bdc'
        carEventsRepository.insert(partnerUUID, carEvent)
    }

    void updateCarEvent(CarEvent carEvent) {
        def partnerUUID = '2169d3f0-98ba-4f73-8f92-015effd24bdc'
        carEventsRepository.updateCarEvent(partnerUUID, carEvent)
    }

    List<CarEvent> findCarEventsForPartnerCar(Car car) {
        def partnerUUID = '2169d3f0-98ba-4f73-8f92-015effd24bdc'
        carEventsRepository.findCarEventsForPartnersCar(partnerUUID, car)
    }
}


