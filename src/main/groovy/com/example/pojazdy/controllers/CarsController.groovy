package com.example.pojazdy.controllers

import com.example.pojazdy.model.ServicePlan
import com.example.pojazdy.model.annotations.HasPartnerRole
import com.example.pojazdy.service.CarsService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.example.pojazdy.model.cars.*

/**
 *
 * @author Jakub Sapiński
 * */
@Slf4j
@RestController
@CompileStatic
@RequestMapping("/cars")
class CarsController {

    private final CarsService carsService

    @Autowired
    CarsController(CarsService carsService) {
        this.carsService = carsService
    }
    @HasPartnerRole
    @GetMapping
    List<Car> cars() {
        carsService.partnerCars()
    }

    @GetMapping("/carMake")
    List<String> carMakes() {
        carsService.getCarsMake()
    }

    @GetMapping("/servicePlanCars/{servicePlanId}")
    List<Car> getServicePlanCars(@PathVariable("servicePlanId") int servicePlanId){
        carsService.servicePlanCars(servicePlanId)
    }

    @GetMapping("/removeServicePlanCars/{carId}")
    void removeCarFromServicePlan(@PathVariable("carId") int carId){
        carsService.removeCarFromServicePlan(carId)
    }

    @GetMapping("/carModel/{carMake}")
    List<String> carModel(@PathVariable("carMake") String carMake) {
        carsService.getCarModels(carMake)
    }

    @GetMapping("/{carId}")
    Car car(@PathVariable("carId") String carId) {
        carsService.specificCar(carId as int)
    }

    @PostMapping
    void addCar(@RequestBody Car car) {
        carsService.addCar(car)
    }

    @PutMapping("/{carId}")
    void updateCar(@PathVariable("carId") String carId, @RequestBody Car car) {
        car.carId = carId as int
        carsService.updateCar(car)
    }

    @PutMapping("/{carId}/editCarUser")
    void updateCarUser(@PathVariable("carId") String carId, @RequestBody CarUser carUser) {
        carUser
        carUser.carId = carId as int
        carsService.updateCarUser(carUser)
    }

    @PutMapping("/{carId}/add/{driverUUID}")
    void appendDriverToCar(@PathVariable("carId") String carId, @PathVariable("driverUUID") String driverUUID, @RequestBody CarUser carUser) {
        carsService.appendDriverToCar(carId as int, driverUUID, carUser)
    }
}
