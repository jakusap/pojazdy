package com.example.pojazdy.controllers

import com.example.pojazdy.model.Driver
import com.example.pojazdy.model.annotations.HasPartnerRole
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.example.pojazdy.service.DriversService

/**
 *
 * @author Jakub Sapiński
 * */

@Slf4j
@RestController
@CompileStatic
@RequestMapping("/drivers")
class DriversController {
    private final DriversService driversService

    @Autowired
    DriversController(DriversService driversService) {
        this.driversService = driversService
    }

    @HasPartnerRole
    @GetMapping
    List<Driver> drivers() {
        driversService.partnerDrivers();
    }

    @HasPartnerRole
    @GetMapping("/{driverUUID}")
    Driver driver(@PathVariable("driverUUID") String driverUUID) {
        driversService.specificDriver(driverUUID)
    }

    @HasPartnerRole
    @PostMapping
    void addDriver(@RequestBody Driver driver) {
        driversService.addDriver(driver)
    }

    @HasPartnerRole
    @PutMapping("/{driverUUID}")
    void updateDriver(@PathVariable("driverUUID") String driverUUID, @RequestBody Driver driver) {
        driver.driverUUID = driverUUID
        driversService.updateDriver(driver)
    }
}
