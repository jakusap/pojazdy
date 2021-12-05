package com.example.pojazdy.repository

import com.example.pojazdy.exceptions.PojazdyException
import com.example.pojazdy.model.cars.Car
import com.example.pojazdy.model.events.ServiceEvent
import com.example.pojazdy.model.events.eventTypes.PartnerServiceEventList
import com.example.pojazdy.model.events.eventTypes.PartnerServiceEvents
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
 */

@Repository
@CompileStatic
class ServicePlanRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate
    private final ServicePlanMapper servicePlanMapper
    private final ServiceEventMapper serviceEventMapper
    private final PartnerServiceEventsMapper partnerServiceEventsMapper

    @Autowired
    ServicePlanRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource)
        this.servicePlanMapper = new ServicePlanMapper()
        this.serviceEventMapper = new ServiceEventMapper()
        this.partnerServiceEventsMapper = new PartnerServiceEventsMapper()

    }

    void insert(String partnerUUID, ServicePlan servicePlan) {
        try {
            def params = setInsertParams(partnerUUID, servicePlan)
            jdbcTemplate.update(Queries.INSERT_INTO_SERVICE_PLANS, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    void insertServiceEvent(String partnerUUID, ServiceEvent serviceEvent) {
        try {
            def params = setServiceEventInsertParams(partnerUUID, serviceEvent)
            jdbcTemplate.update(Queries.INSERT_INTO_SERVICE_EVENTS, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    void updateServicePlan(String partnerUUID, ServicePlan servicePlan) {
        try {
            def params = setInsertParams(partnerUUID, servicePlan)
            params.SERVICE_PLAN_ID = servicePlan.servicePlanId
            jdbcTemplate.update(Queries.UPDATE_SERVICE_PLAN, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    void updateServiceEvent(String partnerUUID, ServiceEvent serviceEvent) {
        try {
            def params = setServiceEventInsertParams(partnerUUID, serviceEvent)
            params.ORDER_NO = serviceEvent.orderNumber
            jdbcTemplate.update(Queries.UPDATE_SERVICE_EVENT, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    void removeServiceEvent(int servicePlanId, int orderNumber) {
        try {
            def params = [
                    PLAN_ID : servicePlanId,
                    ORDER_NO: orderNumber
            ]
            jdbcTemplate.update(Queries.DELETE_SERVICE_EVENT, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    List<ServicePlan> findServicePlansForPartner(String partnerUUID) {
        try {
            def params = [PARTNER_UUID: UUID.fromString(partnerUUID)]
            def sqlQuery = Queries.SELECT_SERVICE_PLANS_FOR_PARTNER
            jdbcTemplate.query(sqlQuery, params, servicePlanMapper)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    ServicePlan findSpecificServicePlanDetails(String partnerUUID, int servicePlanId) {
        try {
            def params = [
                    PARTNER_UUID: UUID.fromString(partnerUUID),
                    PLAN_ID     : servicePlanId
            ]
            def sqlQuery = Queries.SELECT_SERVICE_PLANS_FOR_PARTNER + Queries.BY_PLAN_ID
            jdbcTemplate.queryForObject(sqlQuery, params, servicePlanMapper)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    List<ServiceEvent> findPartnerServicePlanEvents(String partnerUUID, int carId) {
        try {
            def params = [
                    PARTNER_UUID: UUID.fromString(partnerUUID),
                    CAR_ID      : carId
            ]
            def sqlQuery = Queries.SELECT_SERVICE_PLANS_EVENTS_FOR_PARTNER
            jdbcTemplate.query(sqlQuery, params, serviceEventMapper)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    ServicePlan findServicePlansForSpecificCar(String partnerUUID, int carId) {
        try {
            def params = [
                    PARTNER_UUID: UUID.fromString(partnerUUID),
                    CAR_ID      : carId,
            ]
            def sqlQuery = Queries.SELECT_SERVICE_PLANS_FOR_PARTNER_CAR
            jdbcTemplate.queryForObject(sqlQuery, params, servicePlanMapper)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    List<ServiceEvent> findServiceEventsForSpecificPlan(int servicePlanId) {
        try {
            def params = [
                    PLAN_ID: servicePlanId,
            ]
            def sqlQuery = Queries.SELECT_SERVICE_EVENTS_FOR_SERVICE_PLAN
            jdbcTemplate.query(sqlQuery, params, serviceEventMapper)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }

    }


    private static Map<String, Object> setInsertParams(String partnerUUID, ServicePlan servicePlan) {
        [
                PARTNER_UUID: UUID.fromString(partnerUUID),
                CAR_MAKE    : servicePlan.carMake,
                CAR_MODEL   : servicePlan.carModel,
                NAME        : servicePlan.servicePlanName,
                IS_ACTIVE   : servicePlan.isActive
        ] as Map<String, Object>
    }

    private static Map<String, Object> setServiceEventInsertParams(String partnerUUID, ServiceEvent serviceEvent) {
        [
                PLAN_ID             : serviceEvent.planId,
                MILEAGE             : serviceEvent.mileage,
                NOTIFICATION_MILEAGE: serviceEvent.mileageNotification,
                COMMENTS            : serviceEvent.comments,
                PERIOD              : serviceEvent.period,
                NOTIFICATION_PERIOD : serviceEvent.periodNotification
        ] as Map<String, Object>
    }

    private class ServicePlanMapper implements RowMapper<ServicePlan> {

        @Override
        ServicePlan mapRow(ResultSet rs, int rowNum) throws SQLException {
            def servicePlan = new ServicePlan(
                    servicePlanId: rs.getInt("ID"),
                    carMake: rs.getString("CAR_MAKE"),
                    carModel: rs.getString("MODEL"),
                    servicePlanName: rs.getString("NAME"),
                    isActive: rs.getBoolean("IS_ACTIVE"),
            )
            servicePlan
        }
    }

    private class ServiceEventMapper implements RowMapper<ServiceEvent> {

        @Override
        ServiceEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
            def serviceEvent = new ServiceEvent(
                    planId: rs.getInt("PLAN_ID"),
                    orderNumber: rs.getInt("ORDER_NO"),
                    mileage: rs.getInt("MILEAGE"),
                    period: rs.getTimestamp("PERIOD"),
                    comments: rs.getString("COMMENTS"),
                    mileageNotification: rs.getInt("NOTIFICATION_MILEAGE"),
                    periodNotification: rs.getTimestamp("NOTIFICATION_PERIOD")
            )
            serviceEvent
        }
    }

    private class PartnerServiceEventsMapper implements RowMapper<PartnerServiceEventList> {

        @Override
        PartnerServiceEventList mapRow(ResultSet rs, int rowNum) throws SQLException {
            def partnerServiceEvents = new PartnerServiceEventList(
//                    carId: rs.getString("CAR_ID"),
//                    carMake: rs.getString("CAR_MAKE"),
//                    carModel: rs.getString("MODEL"),
//                    registrationNumber: rs.getString("REGISTRATION_NUMBER"),
//                    serviceEvents: new ServiceEvent(
//                            planId: rs.getInt("PLAN_ID"),
//                            orderNumber: rs.getInt("ORDER_NO"),
//                            mileage: rs.getInt("MILEAGE"),
//                            period: rs.getTimestamp("PERIOD"),
//                            comments: rs.getString("COMMENTS"),
//                            mileageNotification: rs.getInt("NOTIFICATION_MILEAGE"),
//                            periodNotification: rs.getTimestamp("NOTIFICATION_PERIOD")
//                    )

            )
            partnerServiceEvents
        }
    }

}
