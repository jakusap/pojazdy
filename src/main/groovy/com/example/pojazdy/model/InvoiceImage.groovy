package com.example.pojazdy.model

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.util.logging.Slf4j
import com.example.pojazdy.exceptions.PojazdyException

import java.nio.file.Path

/**
 * Represents invoice image
 *
 * @author Jakub Sapiński
 */
@Slf4j
class InvoiceImage {

    String uuid

    String partnerUUID

    String partnerCode

    String driverUUID

    String driverFirstName

    String driverLastName

    String filename

    String description

    Date systemEntryDate

    Date deletedByDriverTimestamp

    @JsonIgnore
    Path content

    @JsonIgnore
    String getPath() {
        if (!partnerCode || !driverUUID || !uuid) {
            log.error("Can't generate path. At least one of required fields is null")
            throw new PojazdyException("Can't generate path. At least one of required fields is null")
        }
        "${partnerCode}/driver_invoices/${driverUUID}/${uuid}".toString()
    }
}