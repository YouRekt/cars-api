package pw.react.cars_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import pw.react.cars_api.data_transfer_objects.CarReqDTO;
import pw.react.cars_api.data_transfer_objects.CarRespDTO;
import pw.react.cars_api.services.CarService;

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/")
    @Operation(summary = "Get a page of cars as per the filters.")
    public Page<CarRespDTO> searchCars(@RequestParam Optional<String> brandName,
                                       @RequestParam Optional<String> modelName,
                                       @RequestParam Optional<Long> productionYear,
                                       @RequestParam Optional<String> fuelType,
                                       @RequestParam Optional<Long> fuelCapacity,
                                       @RequestParam Optional<Long> seatCount,
                                       @RequestParam Optional<Long> doorCount,
                                       @RequestParam Optional<BigDecimal> dailyRate,
                                       Pageable pageable) {
        return carService.searchCars(brandName, modelName, productionYear, fuelType, fuelCapacity, seatCount, doorCount, dailyRate, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get car by id.")
    public ResponseEntity<CarRespDTO> getCarById(@PathVariable String id) {
        return carService.getCarById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    @Operation(summary = "Create new car.")
    public ResponseEntity<CarRespDTO> createCar(@RequestBody CarReqDTO dto, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        return ResponseEntity.ok(carService.createCar(dto, authorization));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modify car data.")
    public ResponseEntity<CarRespDTO> updateCar(@PathVariable String id, @RequestBody CarReqDTO dto, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        return carService.updateCar(id, dto, authorization)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete car by id.")
    public ResponseEntity<Void> deleteCar(@PathVariable String id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        if (!carService.deleteCar(id, authorization)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.noContent().build();
    }
}
