package com.example.pojazdy.repository

import com.example.pojazdy.exceptions.PojazdyException
import com.example.pojazdy.model.Driver
import com.example.pojazdy.model.cars.*
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.dao.EmptyResultDataAccessException
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
class CarsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate
    private final CarMapper carMapper
    private final CarsMakeMapper carsMakeMapper
    private final CarUserMapper carUserMapper
    private final CarModelsMapper carModelsMapper

    @Autowired
    CarsRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource)
        this.carMapper = new CarMapper()
        this.carsMakeMapper = new CarsMakeMapper()
        this.carUserMapper = new CarUserMapper()
        this.carModelsMapper = new CarModelsMapper()
    }

    void insert(String partnerUUID, Car car) {
        try {
            def params = setInsertParams(partnerUUID, car)
            jdbcTemplate.update(Queries.INSERT_INTO_CARS, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    void updateCar(String partnerUUID, Car car) {
        try {
            def params = setInsertParams(partnerUUID, car)
            params.CAR_ID = car.carId
            jdbcTemplate.update(Queries.UPDATE_CAR, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    void updateCarUser(String partnerUUID, CarUser carUser) {
        try {
            def params = [
                    ID                   : carUser.id,
                    PARTNER_UUID         : UUID.fromString(partnerUUID),
                    DRIVER_UUID          : UUID.fromString(carUser.driver.driverUUID),
                    CAR_ID               : carUser.carId,
                    PICK_UP_DATETIME     : carUser.pickUpDate,
                    PICK_UP_COMMENT      : carUser.pickUpComment,
                    DROP_OFF_DATETIME    : carUser.dropOffDate,
                    DROP_OFF_COMMENT     : carUser.dropOffComment,
                    RESPONSIBILITY_STATUS: carUser.responsibilityStatus.toString()
            ]
            jdbcTemplate.update(Queries.UPDATE_CAR_USERS, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }

    }

    void appendDriverToCar(String partnerUUID, String driverUUID, int carId, CarUser carUser) {
        try {
            def params = [
                    PARTNER_UUID         : UUID.fromString(partnerUUID),
                    DRIVER_UUID          : UUID.fromString(driverUUID),
                    CAR_ID               : carId,
                    PICK_UP_DATETIME     : carUser.pickUpDate,
                    PICK_UP_COMMENT      : carUser.pickUpComment,
                    DROP_OFF_DATETIME    : carUser.dropOffDate,
                    DROP_OFF_COMMENT     : carUser.dropOffComment,
                    RESPONSIBILITY_STATUS: carUser.responsibilityStatus.toString()
            ]
            jdbcTemplate.update(Queries.INSERT_INTO_CAR_USERS, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    List<Car> findCarsForPartner(String partnerUUID) {
        try {
            def params = [PARTNER_UUID: UUID.fromString(partnerUUID)]
            def sqlQuery = Queries.SELECT_CARS_FOR_PARTNER
            jdbcTemplate.query(sqlQuery, params, carMapper)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    List<Car> findCarsForServicePlan(String partnerUUID, int servicePlanId) {
        try {
            def params = [
                    PARTNER_UUID   : UUID.fromString(partnerUUID),
                    SERVICE_PLAN_ID: servicePlanId
            ]
            def sqlQuery = Queries.SELECT_CARS_FOR_SERVICE_PLAN
            jdbcTemplate.query(sqlQuery, params, carMapper)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    void removeCarForServicePlan(String partnerUUID, int carId) {
        try {
            def params = [
                    PARTNER_UUID: UUID.fromString(partnerUUID),
                    CAR_ID      : carId
            ]
            def sqlQuery = Queries.UPDATE_CAR_SERVICE_PLAN
            jdbcTemplate.update(sqlQuery, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    List<String> findAllCarsMake() {
        try {
            def sqlQuery = Queries.SELECT_ALL_CARS_MAKE
            jdbcTemplate.query(sqlQuery, carsMakeMapper)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    List<String> findAllCarModels(String carMake) {
        try {
            def params = [CAR_MAKE: carMake]
            def sqlQuery = Queries.SELECT_ALL_CAR_MODELS_BY_CAR_MAKE
            jdbcTemplate.query(sqlQuery, params, carModelsMapper)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    Car findCarByCarId(String partnerUUID, int carId) {
        try {
            def params = [
                    PARTNER_UUID: UUID.fromString(partnerUUID),
                    CAR_ID      : carId
            ]
            def sqlQuery = Queries.SELECT_CAR_BY_CAR_ID
            jdbcTemplate.queryForObject(sqlQuery, params, carMapper)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    List<CarUser> findCarDriverByPartnerAndCarId(String partnerUUID, int carId) {
        try {
            def params = [
                    PARTNER_UUID: UUID.fromString(partnerUUID),
                    CAR_ID      : carId
            ]
            jdbcTemplate.query(Queries.SELECT_CAR_DRIVERS, params, carUserMapper)
        } catch (EmptyResultDataAccessException ignored) {
            null
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    private static Map<String, Object> setInsertParams(String partnerUUID, Car car) {
        [
                PARTNER_UUID       : UUID.fromString(partnerUUID),
                CAR_MAKE           : car.carMake,
                CAR_MODEL          : car.carModel,
                CAR_VIN            : car.carVin,
                REGISTRATION_NUMBER: car.registrationNumber,
                REGISTRATION_DATE  : car.registrationDate,
                DESCRIPTION        : car.description,
                PRODUCTION_YEAR    : car.productionYear,
                ADMISSION_DATE     : car.admissionDate,
                SERVICE_PLAN_ID    : car.servicePlanId,
                WITHDRAWAL_DATE    : car.withdrawalDate
        ] as Map<String, Object>
    }


    private class CarMapper implements RowMapper<Car> {

        @Override
        Car mapRow(ResultSet rs, int rowNum) throws SQLException {
            def car = new Car(
                    carId: rs.getInt("CAR_ID"),
                    carMake: rs.getString("CAR_MAKE"),
                    carModel: rs.getString("MODEL"),
                    carVin: rs.getString("VIN"),
                    registrationNumber: rs.getString("REGISTRATION_NUMBER"),
                    registrationDate: rs.getDate("REGISTRATION_DATE"),
                    description: rs.getString("DESCRIPTION"),
                    productionYear: rs.getDate("PRODUCTION_YEAR"),
                    servicePlanId: rs.getInt("SERVICE_PLAN_ID"),
                    admissionDate: rs.getTimestamp("ADMISSION_DATE"),
                    withdrawalDate: rs.getTimestamp("WITHDRAWAL_DATE"),
            )
            car
        }
    }

    private class CarsMakeMapper implements RowMapper<String> {

        @Override
        String mapRow(ResultSet rs, int rowNum) throws SQLException {
            def carMake = rs.getString("CAR_MAKE")
            carMake
        }
    }

    private class CarModelsMapper implements RowMapper<String> {

        @Override
        String mapRow(ResultSet rs, int rowNum) throws SQLException {
            def carModel = rs.getString("MODEL")
            carModel
        }
    }

    private class CarUserMapper implements RowMapper<CarUser> {

        @Override
        CarUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            def carUser = new CarUser(
                    id: rs.getInt("ID"),
                    driver: new Driver(
                            driverUUID: rs.getString("DRIVER_UUID"),
                            firstName: rs.getString("FIRST_NAME"),
                            lastName: rs.getString("LAST_NAME"),
                            email: rs.getString("EMAIL"),
                            phoneNumber: rs.getString("PHONE")
                    ),
                    carId: rs.getInt("CAR_ID"),
                    pickUpDate: rs.getTimestamp("PICK_UP_DATETIME"),
                    dropOffDate: rs.getTimestamp("DROP_OFF_DATETIME"),
                    pickUpComment: rs.getString("PICK_UP_COMMENT"),
                    dropOffComment: rs.getString("DROP_OFF_COMMENT"),
                    responsibilityStatus: ResponsibilityStatus.valueOf(rs.getString("RESPONSIBILITY_STATUS"))
            )
            carUser
        }
    }
}
