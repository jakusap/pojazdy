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

    @HasPartnerRole
    @GetMapping("/carMake")
    List<String> carMakes() {
        carsService.getCarsMake()
    }

    @HasPartnerRole
    @GetMapping("/servicePlanCars/{servicePlanId}")
    List<Car> getServicePlanCars(@PathVariable("servicePlanId") int servicePlanId){
        carsService.servicePlanCars(servicePlanId)
    }

    @HasPartnerRole
    @PostMapping("/servicePlanCars")
    List<Car> servicePlanCars(@RequestBody ServicePlan servicePlan){
        def cars = carsService.availableServicePlanCars(servicePlan)
        cars
    }

    @HasPartnerRole
    @GetMapping("/removeServicePlanCars/{carId}")
    void removeCarFromServicePlan(@PathVariable("carId") int carId){
        carsService.removeCarFromServicePlan(carId)
    }

    @HasPartnerRole
    @GetMapping("/addCarForServicePlan/{carId}/{servicePlanId}")
    void addCarForServicePlan(@PathVariable("carId") int carId, @PathVariable("servicePlanId") int servicePlanId){
        carsService.addCarForServicePlan(carId, servicePlanId)
    }

    @HasPartnerRole
    @GetMapping("/carModel/{carMake}")
    List<String> carModel(@PathVariable("carMake") String carMake) {
        carsService.getCarModels(carMake)
    }

    @HasPartnerRole
    @GetMapping("/{carId}")
    Car car(@PathVariable("carId") String carId) {
        carsService.specificCar(carId as int)
    }

    @HasPartnerRole
    @PostMapping
    void addCar(@RequestBody Car car) {
        carsService.addCar(car)
    }

    @HasPartnerRole
    @PutMapping("/{carId}")
    void updateCar(@PathVariable("carId") String carId, @RequestBody Car car) {
        car.carId = carId as int
        carsService.updateCar(car)
    }

    @HasPartnerRole
    @PutMapping("/{carId}/editCarUser")
    void updateCarUser(@PathVariable("carId") String carId, @RequestBody CarUser carUser) {
        carUser
        carUser.carId = carId as int
        carsService.updateCarUser(carUser)
    }

    @HasPartnerRole
    @PutMapping("/{carId}/add/{driverUUID}")
    void appendDriverToCar(@PathVariable("carId") String carId, @PathVariable("driverUUID") String driverUUID, @RequestBody CarUser carUser) {
        carsService.appendDriverToCar(carId as int, driverUUID, carUser)
    }
}
