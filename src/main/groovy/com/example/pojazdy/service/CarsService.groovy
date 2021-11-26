package com.example.pojazdy.service

import com.example.pojazdy.exceptions.BadRequestException
import com.example.pojazdy.model.cars.*
import com.example.pojazdy.repository.CarsRepository
import com.example.pojazdy.repository.DriversRepository
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 *
 * @author Jakub Sapiński
 */
@Slf4j
@Service
@CompileStatic
class CarsService {
    private final DriversRepository driversRepository
    private final CarsRepository carsRepository
    private final LoginService loginService

    @Autowired
    CarsService(DriversRepository driversRepository, CarsRepository carsRepository, LoginService loginService) {
        this.driversRepository = driversRepository
        this.carsRepository = carsRepository
        this.loginService = loginService
    }

    void addCar(Car car) {
        def partnerUUID = loginService.loginPartnerUUID()
        if (!car.isValid()) {
            throw new BadRequestException("Car is invalid!")
        }
        carsRepository.insert(partnerUUID, car)
    }

    List<Car> partnerCars() {
        def partnerUUID = loginService.loginPartnerUUID()
        def cars = carsRepository.findCarsForPartner(partnerUUID)
        if (cars) {
            cars.each {
                it.carUsers = carsRepository.findCarDriverByPartnerAndCarId(partnerUUID, it.carId)
                if (it.carUsers) {
                    it.driver = it.carUsers[0].driver
                }
            }
        }
        cars
    }

    List<String> getCarsMake() {
        def carsMake = carsRepository.findAllCarsMake()
        carsMake
    }

    List<String> getCarModels(String carMake) {
        def carModels = carsRepository.findAllCarModels(carMake)
        carModels
    }

    Car specificCar(int carId) {
        def partnerUUID = loginService.loginPartnerUUID()
        def car = carsRepository.findCarByCarId(partnerUUID, carId)
        car.carUsers = carsRepository.findCarDriverByPartnerAndCarId(partnerUUID, carId)
        car
    }

    void updateCar(Car car) {
        def partnerUUID = loginService.loginPartnerUUID()
        if (!car.isValid()) {
            throw new BadRequestException("Car is invalid!")
        }
        carsRepository.updateCar(partnerUUID, car)
    }

    void updateCarUser(CarUser carUser) {
        def partnerUUID = loginService.loginPartnerUUID()
        carsRepository.updateCarUser(partnerUUID, carUser)
    }

    void appendDriverToCar(int carId, String driverUUID, CarUser carUser) {
        def partnerUUID = loginService.loginPartnerUUID()
        log.info("Appending driver UUID {} to carId: {} for partner: {}", driverUUID, carId, partnerUUID)
        carsRepository.appendDriverToCar(partnerUUID, driverUUID, carId, carUser)
    }
}
