package com.example.pojazdy.model.events.eventTypes

import com.example.pojazdy.model.Partner
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 *
 * @author Jakub Sapiński
 * */
class EventTypes {

    Partner partner

    String code

    String name

    String category

    @JsonIgnore
    Boolean isActive

    @JsonIgnore
    boolean isValid() {
        if (!this.isActive) {
            return false
        }
        return true
    }
}
