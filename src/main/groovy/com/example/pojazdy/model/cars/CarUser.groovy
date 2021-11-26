package com.example.pojazdy.model.cars

import com.example.pojazdy.model.Driver
import java.sql.Timestamp as TS

/**
 *
 * @author Jakub Sapiński
 */
class CarUser {

    int id

    Driver driver

    int carId

    TS pickUpDate

    TS dropOffDate

    String pickUpComment

    String dropOffComment

    ResponsibilityStatus responsibilityStatus
}
