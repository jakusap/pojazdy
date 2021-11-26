package com.example.pojazdy.repository

import com.example.pojazdy.exceptions.PojazdyException
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import com.example.pojazdy.model.Driver
import javax.sql.DataSource
import java.sql.ResultSet
import java.sql.SQLException

/**
 *
 * @author Jakub Sapiński
 */

@Repository
@CompileStatic
class DriversRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate
    private final DriverMapper driverMapper

    @Autowired
    DriversRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource)
        this.driverMapper = new DriverMapper()
    }

    String insertDriver(String partnerUUID, Driver driver) {
        driver.driverUUID = UUID.randomUUID().toString()
        try {
            def params = setInsertParams(partnerUUID, driver)
            jdbcTemplate.update(Queries.INSERT_INTO_DRIVERS, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
        driver.driverUUID
    }

    void updateDriver(String partnerUUID, Driver driver) {
        try {
            def params = setInsertParams(partnerUUID, driver)
            jdbcTemplate.update(Queries.UPDATE_DRIVER, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    List<Driver> findDriversForPartner(String partnerUUID) {
        def params = [PARTNER_UUID: UUID.fromString(partnerUUID)]
        def sqlQuery = Queries.SELECT_DRIVERS_FOR_PARTNER
        jdbcTemplate.query(sqlQuery, params, driverMapper)
    }

    Driver findDriverByDriverUUID(String partnerUUID, String driverUUID) {
        def params = [PARTNER_UUID: UUID.fromString(partnerUUID),
                      DRIVER_UUID : UUID.fromString(driverUUID)]
        try {
            jdbcTemplate.queryForObject(Queries.SELECT_DRIVERS_FOR_PARTNER + Queries.BY_DRIVER_UUID, params, driverMapper)
        } catch (EmptyResultDataAccessException ignored) {
            null
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    private static Map<String, Object> setInsertParams(String partnerUuid, Driver driver) {
        [
                PARTNER_UUID       : UUID.fromString(partnerUuid),
                DRIVER_UUID        : UUID.fromString(driver.driverUUID),
                FIRST_NAME         : driver.firstName,
                LAST_NAME          : driver.lastName,
                EMAIL              : driver.email,
                PHONE              : driver.phoneNumber,
        ] as Map<String, Object>
    }

    private class DriverMapper implements RowMapper<Driver> {


        @Override
        Driver mapRow(ResultSet rs, int rowNum) throws SQLException {
            def driver = new Driver(
                    driverUUID: rs.getString("DRIVER_UUID"),
                    firstName: rs.getString("FIRST_NAME"),
                    lastName: rs.getString("LAST_NAME"),
                    phoneNumber: rs.getString("PHONE"),
                    email: rs.getString("EMAIL"),
            )
            driver
        }
    }
}
