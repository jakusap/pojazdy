package com.example.pojazdy.model
import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.util.logging.Slf4j

/**
 *
 * @author Jakub Sapiński
 * */

@Slf4j
class Driver {

    String driverUUID

    String firstName

    String lastName

    String email

    String phoneNumber
}
