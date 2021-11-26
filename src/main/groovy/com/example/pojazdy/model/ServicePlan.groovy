package com.example.pojazdy.model

import com.example.pojazdy.model.cars.Car
import com.example.pojazdy.model.events.ServiceEvent
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 *
 * @author Jakub Sapiński
 * */
class ServicePlan {

    int servicePlanId

    String carMake

    String carModel

    String servicePlanName

    Boolean isActive

    List<ServiceEvent> serviceEvents

    @JsonIgnore
    boolean isValid(){
        if (!this.isActive)
        {
            return false
        }
        return true
    }


}
