package com.example.pojazdy.repository


import com.example.pojazdy.exceptions.PojazdyException
import com.example.pojazdy.model.cars.Car
import com.example.pojazdy.model.events.CarEvent
import com.example.pojazdy.model.events.ServiceEvent
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import com.example.pojazdy.model.ServicePlan

import javax.sql.DataSource
import java.sql.ResultSet
import java.sql.SQLException

/**
 *
 * @author Jakub Sapiński
 * */

@Repository
@CompileStatic
class CarEventsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate
    private final CarEventMapper carEventMapper

    @Autowired
    CarEventsRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource)
        this.carEventMapper = new CarEventMapper()
    }

    void insert(String partnerUUID, CarEvent carEvent) {
        try {
            def params = setInsertParams(partnerUUID, carEvent)
            jdbcTemplate.update(Queries.INSERT_INTO_CAR_EVENTS, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    void updateCarEvent(String partnerUUID, CarEvent carEvent) {
        try {
            def params = setInsertParams(partnerUUID, carEvent)
            params.CAR_EVENT_ID = carEvent.carEventId
            jdbcTemplate.update(Queries.UPDATE_CAR_EVENT, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    List<CarEvent> findCarEventsForPartnersCar(String partnerUUID, int carId) {
        try {
            def params = [
                    PARTNER_UUID: UUID.fromString(partnerUUID),
                    CAR_ID: carId
            ]
            def sqlQuery = Queries.SELECT_CAR_EVENTS_FOR_PARTNER_CAR
            jdbcTemplate.query(sqlQuery, params, carEventMapper)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    private static Map<String, Object> setInsertParams(String partnerUUID, CarEvent carEvent) {
        [
                PARTNER_UUID       : UUID.fromString(partnerUUID),
                CAR_ID             : carEvent.carId,
                EVENT_CODE         : carEvent.eventCode,
                PLAN_ID            : carEvent.planId,
                ORDER_NO           : carEvent.orderNumber,
                DATETIME           : carEvent.dateTime,
                MILEAGE            : carEvent.mileage,
                USER_ID            : carEvent.carUserId as int,
                COST               : carEvent.cost
        ] as Map<String, Object>
    }

    private class CarEventMapper implements RowMapper<CarEvent> {

        @Override
        CarEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
            def carEvent = new CarEvent(
                    carEventId: rs.getInt("ID"),
                    carId: rs.getInt("CAR_ID"),
                    eventCodeName: rs.getString("NAME"),
                    eventCode: rs.getString("EVENT_CODE"),
                    planId: rs.getInt("PLAN_ID"),
                    orderNumber: rs.getInt("ORDER_NO"),
                    dateTime: rs.getTimestamp("DATETIME"),
                    mileage: rs.getInt("MILEAGE"),
                    carUserId: rs.getInt("USER_ID"),
                    driverName: rs.getString("FIRST_NAME") + " " + rs.getString("LAST_NAME"),
                    cost: rs.getInt("COST"),
            )
            carEvent
        }
    }
}
