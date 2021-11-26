package com.example.pojazdy.repository

/**
 *
 * @author Jakub Sapiński
 * */

abstract class Queries {

    // Partners
    static final String INSERT_INTO_USER_PARTNERS = """INSERT INTO SYSADM.USER_PARTNERS(PARTNER_UUID, ACS_ID) 
            VALUES (:PARTNER_UUID, :ACS_ID)"""

    static final String UPDATE_PARTNER_BY_UUID = """UPDATE SYSADM.PARTNERS SET NAME = :NAME, EMAIL = :EMAIL, 
            PHONE_NUMBER = :PHONE_NUMBER WHERE 1=1""" + BY_PARTNER_UUID

    static final String SELECT_PARTNERS = """SELECT * 
        FROM SYSADM.PARTNERS WHERE 1=1"""

    static final String INSERT_INTO_PARTNERS = """INSERT INTO SYSADM.PARTNERS(PARTNER_UUID, NAME, EMAIL, PHONE_NUMBER) 
            VALUES (:PARTNER_UUID, :NAME, :EMAIL, :PHONE_NUMBER)"""

    static final String SELECT_PARTNER_BY_ACS_ID = """SELECT P.* FROM SYSADM.PARTNERS P, SYSADM.USER_PARTNERS UP 
        WHERE P.PARTNER_UUID = UP.PARTNER_UUID AND UP.ACS_ID = :ACS_ID"""

    static final String SELECT_PARTNER_BY_UUID = """SELECT * FROM SYSADM.PARTNERS WHERE 1=1""" + BY_PARTNER_UUID

    static final String BY_PARTNER_UUID = """ AND PARTNER_UUID = :PARTNER_UUID"""

    // Drivers

    static final String INSERT_INTO_DRIVERS = """INSERT INTO SYSADM.DRIVERS (PARTNER_UUID, DRIVER_UUID, FIRST_NAME, LAST_NAME, EMAIL, PHONE) 
                            VALUES (:PARTNER_UUID, :DRIVER_UUID, :FIRST_NAME, :LAST_NAME, :EMAIL, :PHONE)"""

    static final String UPDATE_DRIVER = """UPDATE SYSADM.DRIVERS
                            SET FIRST_NAME = :FIRST_NAME, LAST_NAME = :LAST_NAME, EMAIL = :EMAIL, PHONE = :PHONE
                            WHERE DRIVER_UUID = :DRIVER_UUID AND PARTNER_UUID = :PARTNER_UUID"""

    static final String SELECT_DRIVERS_FOR_PARTNER = """SELECT D.* FROM SYSADM.DRIVERS D WHERE D.PARTNER_UUID = :PARTNER_UUID"""

    static final String BY_DRIVER_UUID = """ AND DRIVER_UUID = :DRIVER_UUID """

    // Cars
    static final String INSERT_INTO_CARS = """INSERT INTO SYSADM.CARS (PARTNER_UUID, CAR_MAKE, MODEL, VIN, REGISTRATION_NUMBER, REGISTRATION_DATE, DESCRIPTION, PRODUCTION_YEAR, ADMISSION_DATE, WITHDRAWAL_DATE) 
        VALUES (:PARTNER_UUID, :CAR_MAKE, :CAR_MODEL, :CAR_VIN, :REGISTRATION_NUMBER, :REGISTRATION_DATE, :DESCRIPTION, :PRODUCTION_YEAR, :ADMISSION_DATE, :WITHDRAWAL_DATE)"""

    static final String SELECT_CARS_FOR_PARTNER = """SELECT C.* FROM SYSADM.CARS C WHERE C.PARTNER_UUID = :PARTNER_UUID"""

    static final String SELECT_CARS_USERS_FOR_PARTNER = """SELECT CU.*, C.*, D.* FROM SYSADM.CAR_USERS CU, SYSADM.CARS C, SYSADM.DRIVERS D 
        WHERE CU.PARTNER_UUID = :PARTNER_UUID AND C.CAR_ID = CU.CAR_ID AND CU.DRIVER_UUID = D.DRIVER_UUID AND CU.PARTNER_UUID = D.PARTNER_UUID"""

    static final String SELECT_CAR_BY_CAR_ID = """SELECT C.* FROM SYSADM.CARS C WHERE C.PARTNER_UUID = :PARTNER_UUID AND C.CAR_ID = :CAR_ID"""

    static final String UPDATE_CAR = """UPDATE SYSADM.CARS
                            SET CAR_MAKE = :CAR_MAKE, MODEL = :CAR_MODEL, VIN = :CAR_VIN, REGISTRATION_NUMBER = :REGISTRATION_NUMBER, 
                            REGISTRATION_DATE = :REGISTRATION_DATE, DESCRIPTION = :DESCRIPTION, PRODUCTION_YEAR = :PRODUCTION_YEAR,
                            ADMISSION_DATE = :ADMISSION_DATE, WITHDRAWAL_DATE = :WITHDRAWAL_DATE WHERE CAR_ID = :CAR_ID AND PARTNER_UUID = :PARTNER_UUID"""

    static final String SELECT_ALL_CARS_MAKE = """SELECT M.CAR_MAKE FROM SYSADM.MODELS M ORDER BY M.CAR_MAKE ASC"""

    static final String SELECT_ALL_CAR_MODELS_BY_CAR_MAKE = """SELECT M.MODEL FROM SYSADM.MODELS M WHERE M.CAR_MAKE = :CAR_MAKE ORDER BY M.MODEL ASC"""

    // Car Users

    static final String SELECT_CAR_DRIVERS = """SELECT CU.*, D.* 
        FROM SYSADM.CAR_USERS CU, SYSADM.DRIVERS D
        WHERE CU.PARTNER_UUID = :PARTNER_UUID AND CU.CAR_ID = :CAR_ID AND D.DRIVER_UUID = CU.DRIVER_UUID ORDER BY CU.ID DESC"""

    static final String INSERT_INTO_CAR_USERS = """INSERT INTO SYSADM.CAR_USERS 
        (PARTNER_UUID, DRIVER_UUID, CAR_ID, PICK_UP_DATETIME, DROP_OFF_DATETIME, PICK_UP_COMMENT, DROP_OFF_COMMENT, RESPONSIBILITY_STATUS) 
        VALUES (:PARTNER_UUID, :DRIVER_UUID, :CAR_ID, :PICK_UP_DATETIME, :DROP_OFF_DATETIME, :PICK_UP_COMMENT, :DROP_OFF_COMMENT, :RESPONSIBILITY_STATUS)"""

    static final String UPDATE_CAR_USERS = """UPDATE SYSADM.CAR_USERS 
        SET PICK_UP_DATETIME = :PICK_UP_DATETIME, DROP_OFF_DATETIME = :DROP_OFF_DATETIME, PICK_UP_COMMENT = :PICK_UP_COMMENT, 
        DROP_OFF_COMMENT = :DROP_OFF_COMMENT, RESPONSIBILITY_STATUS = :RESPONSIBILITY_STATUS
        WHERE ID = :ID AND PARTNER_UUID = :PARTNER_UUID AND DRIVER_UUID = :DRIVER_UUID AND CAR_ID = :CAR_ID"""


    // Service Plan

    static final String INSERT_INTO_SERVICE_PLANS = """INSERT INTO SYSADM.SERVICE_PLAN (PARTNER_UUID, CAR_MAKE, MODEL, NAME) 
        VALUES(:PARTNER_UUID, :CAR_MAKE, :CAR_MODEL, :NAME)"""

    static final String UPDATE_SERVICE_PLAN = """UPDATE SYSADM.SERVICE_PLAN SET CAR_MAKE = :CAR_MAKE, MODEL = :CAR_MODEL, NAME = :NAME, IS_ACTIVE = :IS_ACTIVE 
        WHERE PARTNER_UUID = :PARTNER_UUID AND ID = :SERVICE_PLAN_ID"""

    static final String SELECT_SERVICE_PLANS_FOR_PARTNER = """SELECT SP.* FROM SYSADM.SERVICE_PLAN SP WHERE SP.PARTNER_UUID = :PARTNER_UUID"""

    static final String SELECT_SERVICE_PLANS_EVENTS_FOR_PARTNER = """SELECT SP.*, SE.* FROM SYSADM.SERVICE_PLAN SP, SYSADM.SERVICE_EVENTS SE WHERE SE.PLAN_ID = SP.ID 
        AND SP.PARTNER_UUID = :PARTNER_UUID"""

    static final String BY_PLAN_ID = """ AND SP.ID = :PLAN_ID"""

    static final String BY_CAR_MAKE_CAR_MODEL = """ AND SP.CAR_MAKE = :CAR_MAKE AND SP.MODEL = :CAR_MODEL"""

    // ServiceEvent

    static final String SELECT_SERVICE_EVENTS_FOR_SERVICE_PLAN = """SELECT SE.* FROM SYSADM.SERVICE_EVENTS SE WHERE SE.PLAN_ID = :PLAN_ID"""


    // Car Event

    static final String INSERT_INTO_CAR_EVENTS = """INSERT INTO SYSADM.CAR_EVENTS (PARTNER_UUID, CAR_ID, EVENT_CODE, PLAN_ID, ORDER_NO, DATETIME, MILEAGE, USER_ID, COST) 
        VALUES(:PARTNER_UUID, :CAR_ID, :EVENT_CODE, :PLAN_ID, :ORDER_NO, :DATETIME, :MILEAGE, :USER_ID, :COST)"""

    static final String UPDATE_CAR_EVENT = """UPDATE SYSADM.CAR_EVENTS SET PARTNER_UUID = :PARTNER_UUID, CAR_ID = :CAR_ID, EVENT_CODE = :EVENT_CODE, 
        PLAN_ID = :PLAN_ID, ORDER_NO = :ORDER_NO, DATETIME = :DATETIME, MILEAGE = :MILEAGE, USER_ID = :USER_ID, COST = :COST 
        WHERE PARTNER_UUID = :PARTNER_UUID AND ID = :CAR_EVENT_ID"""

    static final String SELECT_CAR_EVENTS_FOR_PARTNER_CAR = """SELECT CE.* FROM SYSADM.CAR_EVENTS CE WHERE CE.PARTNER_UUID = :PARTNER_UUID AND CE.CAR_ID = :CAR_ID"""

}
