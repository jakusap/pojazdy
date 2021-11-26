package com.example.pojazdy.service

import com.example.pojazdy.exceptions.NotFoundException
import com.example.pojazdy.model.Driver
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.example.pojazdy.repository.DriversRepository

/**
 *
 * @author Jakub Sapiński
 */
@Slf4j
@Service
@CompileStatic
@Transactional
class DriversService {

    private final DriversRepository driversRepository
    private final LoginService loginService

    @Autowired
    DriversService(DriversRepository driversRepository, LoginService loginService) {
        this.driversRepository = driversRepository
        this.loginService = loginService
    }

    List<Driver> partnerDrivers() {
        def partnerUUID = loginService.loginPartnerUUID()
        def drivers = driversRepository.findDriversForPartner(partnerUUID)

        drivers
    }

    Driver specificDriver(String driverUUID) {
        def partnerUUID = loginService.loginPartnerUUID()
        def driver = driversRepository.findDriverByDriverUUID(partnerUUID, driverUUID)
        if (!driver) {
            throw new NotFoundException("Driver not found") as Throwable
        }

        driver
    }

    String addDriver(Driver driver) {
        def partnerUUID = loginService.loginPartnerUUID()
        def driverUUID = driversRepository.insertDriver(partnerUUID, driver)
        driverUUID
    }

    void updateDriver(Driver driver) {
        def partnerUUID = loginService.loginPartnerUUID()
        driversRepository.updateDriver(partnerUUID, driver)
    }
}
