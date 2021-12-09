package com.example.pojazdy.model.documents

import com.example.pojazdy.exceptions.PojazdyException
import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.util.logging.Slf4j

import java.nio.file.Path
import java.sql.Timestamp as TS

/**
 *
 * @author Jakub Sapiński
 */
@Slf4j
class Document {

    int documentId

    String partnerUUID

    String typeCode

    String driverUUID

    String filename

    String description

    TS systemEntryDate

    @JsonIgnore
    Path content

    @JsonIgnore
    String getPath() {
        if (!partnerUUID || !filename) {
            log.error("Can't generate path. At least one of required fields is null")
            throw new PojazdyException("Can't generate path. At least one of required fields is null")
        }
        "${partnerUUID}/${documentId}-${filename}".toString()
    }

}
