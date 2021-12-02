package com.example.pojazdy.model.events

import java.sql.Timestamp as TS

/**
 *
 * @author Jakub Sapiński
 * */
class CarEvent {

    int carEventId

    String partnerUUID

    int carId

    String eventCodeName

    String eventCode

    String planId

    String orderNumber

    TS dateTime

    int mileage

    String carUserId

    String driverName

    int cost


}
