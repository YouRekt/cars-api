package pw.react.cars_api.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import pw.react.cars_api.data_transfer_objects.CarReqDTO;
import pw.react.cars_api.data_transfer_objects.CarRespDTO;
import pw.react.cars_api.services.CarService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<CarRespDTO> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarRespDTO> getCarById(@PathVariable String id) {
        return carService.getCarById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CarRespDTO> createCar(@RequestBody CarReqDTO dto, @RequestHeader(name = "X-Auth") String authorization) {
        return ResponseEntity.ok(carService.createCar(dto, authorization));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarRespDTO> updateCar(@PathVariable String id, @RequestBody CarReqDTO dto, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        return carService.updateCar(id, dto, authorization)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable String id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        carService.deleteCar(id, authorization);
        return ResponseEntity.noContent().build();
    }
}
