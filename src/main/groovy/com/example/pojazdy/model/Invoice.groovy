package com.example.pojazdy.model

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.util.logging.Slf4j
import com.example.pojazdy.exceptions.PojazdyException

import java.nio.file.Path
import java.sql.Timestamp

/**
 * Represents invoice image
 *
 * @author Jakub Sapiński
 */
@Slf4j
class Invoice {

    String uuid

    String partnerUUID

    String driverUUID

    String driverFirstName

    String driverLastName

    String filename

    String description

    Timestamp systemEntryDate

    Timestamp deletedByDriverTimestamp

    @JsonIgnore
    Path content

    @JsonIgnore
    String getPath() {
        if (!partnerUUID || !uuid) {
            log.error("Can't generate path. At least one of required fields is null")
            throw new PojazdyException("Can't generate path. At least one of required fields is null")
        }
        "${partnerUUID}/invoices/${uuid}".toString()
    }
}