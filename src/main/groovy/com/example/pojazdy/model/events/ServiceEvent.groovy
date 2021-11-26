package com.example.pojazdy.model.events

import java.sql.Timestamp

/**
 *
 * @author Jakub Sapiński
 * */
class ServiceEvent {

    int planId

    int orderNumber

    int mileage

    Timestamp period

    String comments

    int mileageNotification

    Timestamp periodNotification
}
