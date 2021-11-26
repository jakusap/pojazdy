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

    String eventCode

    int planId

    int orderNumber

    TS dateTime

    int mileage

    int carUserId

    int cost


}
