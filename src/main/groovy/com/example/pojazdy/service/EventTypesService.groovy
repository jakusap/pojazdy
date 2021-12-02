package com.example.pojazdy.service

import com.example.pojazdy.exceptions.BadRequestException
import com.example.pojazdy.model.events.eventTypes.EventTypes
import com.example.pojazdy.repository.EventTypesRepository
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
@CompileStatic
class EventTypesService {

    private final EventTypesRepository eventTypesRepository
    private final LoginService loginService

    @Autowired
    EventTypesService(EventTypesRepository eventTypesRepository, LoginService loginService) {
        this.eventTypesRepository = eventTypesRepository
        this.loginService = loginService
    }

    List<EventTypes> eventTypes() {
        def partnerUUID = loginService.loginPartnerUUID()
        def events = eventTypesRepository.findEventTypes(partnerUUID)
        events
    }
    void addEventTypes(EventTypes eventTypes) {
        def partnerUUID = loginService.loginPartnerUUID()
        if (!eventTypes.isValid()) {
            throw new BadRequestException("EventType is invalid!")
        }
        eventTypesRepository.insert(partnerUUID, eventTypes)
    }
}
