package com.example.pojazdy.model.documents

import groovy.util.logging.Slf4j
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

}
