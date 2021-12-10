package com.example.pojazdy.repository

import com.example.pojazdy.exceptions.PojazdyException
import com.example.pojazdy.model.Driver
import com.example.pojazdy.model.Invoice
import com.example.pojazdy.model.documents.Document
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

import javax.sql.DataSource
import java.sql.ResultSet
import java.sql.SQLException


/**
 *
 * @author Jakub Sapiński
 */

@Repository
@CompileStatic
class InvoiceRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate
    private final InvoicesMapper invoicesMapper

    @Autowired
    InvoiceRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource)
        this.invoicesMapper = new InvoicesMapper()
    }


    private static Map<String, Object> setInsertParams(Invoice invoice) {
        [
                PARTNER_UUID     : UUID.fromString(invoice.partnerUUID),
                DRIVER_UUID      : UUID.fromString(invoice.driverUUID),
                FILENAME         : invoice.filename,
                DESCRIPTION      : invoice.description,
                SYSTEM_ENTRY_DATE: invoice.systemEntryDate
        ] as Map<String, Object>
    }

    String insert(Invoice invoice) {
        invoice.uuid = UUID.randomUUID().toString()
        try {
            def params = setInsertParams(invoice)
            params.UUID = UUID.fromString(invoice.uuid)
            jdbcTemplate.update(Queries.INSERT_INTO_INVOICES, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
        invoice.uuid
    }

    Invoice findInvoiceByUUID(String partnerUUID, String invoiceUUID) {
        try {
            def params = [
                    PARTNER_UUID: UUID.fromString(partnerUUID),
                    UUID : UUID.fromString(invoiceUUID)
            ]
            def sqlQuery = Queries.SELECT_INVOICE_BY_UUID
            jdbcTemplate.queryForObject(sqlQuery, params, invoicesMapper)
        }
        catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }


    private class InvoicesMapper implements RowMapper<Invoice> {

        @Override
        Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {
            def invoice = new Invoice(
                    uuid: rs.getString("INVOICE_IMAGE_UUID"),
                    partnerUUID: rs.getString("PARTNER_UUID"),
                    driverUUID: rs.getString("DRIVER_UUID"),
                    filename: rs.getString("FILENAME"),
                    description: rs.getString("DESCRIPTION"),
                    systemEntryDate: rs.getTimestamp("SYSTEM_ENTRY_DATE"),
                    deletedByDriverTimestamp: rs.getTimestamp("DELETED_BY_DRIVER_TIMESTAMP")
            )
            invoice
        }
    }
}
