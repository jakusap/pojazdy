package com.example.pojazdy.repository

import com.example.pojazdy.model.Document
import com.example.pojazdy.model.cars.Car
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

import javax.print.Doc
import javax.sql.DataSource
import java.sql.ResultSet
import java.sql.SQLException


/**
 *
 * @author Jakub Sapiński
 */

@Repository
@CompileStatic
class DocumentsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate
    private final DocumentsMapper documentsMapper

    @Autowired
    DocumentsRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource)
        this.documentsMapper = new DocumentsMapper()
    }

    private static Map<String, Object> setInsertParams(Document document) {
        [
                PARTNER_UUID       : UUID.fromString(document.partnerUUID),
                TYPE_CODE          : document.typeCode,
                DRIVER_UUID        : UUID.fromString(document.driverUUID),
                FILENAME           : document.filename,
                DESCRIPTION        : document.description,
                SYSTEM_ENTRY_DATE  : document.systemEntryDate
        ] as Map<String, Object>
    }


    private class DocumentsMapper implements RowMapper<Document> {

        @Override
        Document mapRow(ResultSet rs, int rowNum) throws SQLException {
            def document = new Document(
                    documentId: rs.getInt("ID"),
                    partnerUUID: rs.getString("PARTNER_UUID"),
                    typeCode: rs.getString("TYPE_CODE"),
                    driverUUID: rs.getString("DRIVER_UUID"),
                    filename: rs.getString("FILENAME"),
                    description: rs.getString("DESCRIPTION"),
                    systemEntryDate: rs.getTimestamp("SYSTEM_ENTRY_DATE"),
            )
            document
        }
    }
}
