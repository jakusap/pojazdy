package com.example.pojazdy.model.cars

import com.example.pojazdy.model.Driver
import com.example.pojazdy.model.cars.CarUser
import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.util.logging.Slf4j
import java.sql.Timestamp as TS

/**
 *
 * @author Jakub Sapiński
 */
@Slf4j
class Car {
    int carId

    String carMake

    String carModel

    String carVin

    String registrationNumber

    Date registrationDate

    String description

    Date productionYear

    TS admissionDate

    Date withdrawalDate

    List<CarUser> carUsers

    int servicePlanId

    Driver driver


    @JsonIgnore
    boolean isValid(){
        if (!this.carMake || !this.carModel || !this.registrationNumber || !this.registrationDate || !this.admissionDate)
        {
            return false
        }
        return true
    }
}
