package com.example.pojazdy.controllers

import com.example.pojazdy.model.Driver
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

    @GetMapping
    List<Driver> drivers() {
        driversService.partnerDrivers();
    }

    @GetMapping("/{driverUUID}")
    Driver driver(@PathVariable("driverUUID") String driverUUID) {
        driversService.specificDriver(driverUUID)
    }

    @PostMapping
    void addDriver(@RequestBody Driver driver) {
        driversService.addDriver(driver)
    }

    @PutMapping("/{driverUUID}")
    void updateDriver(@PathVariable("driverUUID") String driverUUID, @RequestBody Driver driver) {
        driver.driverUUID = driverUUID
        driversService.updateDriver(driver)
    }
}
