package com.example.pojazdy.controllers

import com.example.pojazdy.model.annotations.HasPartnerRole
import com.example.pojazdy.model.cars.Car
import com.example.pojazdy.model.events.eventTypes.EventTypes
import com.example.pojazdy.service.EventTypesService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
@CompileStatic
@RequestMapping("/event_types")
class EventTypesController {

    private final EventTypesService eventTypesService

    @Autowired
    EventTypesController(EventTypesService eventTypesService) {
        this.eventTypesService = eventTypesService
    }

    @HasPartnerRole
    @GetMapping
    List<EventTypes> eventTypes() {
        eventTypesService.eventTypes()
    }

    @HasPartnerRole
    @PostMapping
    void addEventType(@RequestBody EventTypes eventTypes) {
        eventTypesService.addEventTypes(eventTypes)
    }
}
