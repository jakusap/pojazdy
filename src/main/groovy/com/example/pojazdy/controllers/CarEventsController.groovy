package com.example.pojazdy.controllers

import com.example.pojazdy.model.cars.Car
import com.example.pojazdy.model.events.CarEvent
import com.example.pojazdy.service.CarEventsService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 *
 * @author Jakub Sapiński
 * */
@Slf4j
@RestController
@CompileStatic
@RequestMapping("/car_events")
class CarEventsController {
    private final CarEventsService carEventsService

    @Autowired
    CarEventsController(CarEventsService carEventsService) {
        this.carEventsService = carEventsService
    }

    @GetMapping
    List<CarEvent> carEvents(@RequestBody Car car) {
        carEventsService.findCarEventsForPartnerCar(car)
    }

    @PostMapping()
    void addCarEvent(@RequestBody CarEvent carEvent) {
        carEventsService.addCarEvent(carEvent)
    }

    @PutMapping("/update/{carEventId}")
    void updateCarEvent(@PathVariable("carEventId") int carEventId, @RequestBody CarEvent carEvent) {
        carEvent.carEventId = carEventId
        carEventsService.updateCarEvent(carEvent)
    }
}
