package pw.react.cars_api.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import pw.react.cars_api.data_transfer_objects.FuelTypeReqDTO;
import pw.react.cars_api.data_transfer_objects.FuelTypeRespDTO;
import pw.react.cars_api.services.FuelTypeService;

import java.util.List;

@RestController
@RequestMapping("/fuel-types")
public class FuelTypeController {

    private final FuelTypeService fuelTypeService;

    public FuelTypeController(FuelTypeService fuelTypeService) {
        this.fuelTypeService = fuelTypeService;
    }

    @GetMapping
    public List<FuelTypeRespDTO> getAllFuelTypes() {
        return fuelTypeService.getAllFuelTypes();
    }

    @PostMapping
    public ResponseEntity<FuelTypeRespDTO> addFuelType(@RequestBody FuelTypeReqDTO dto, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        return ResponseEntity.ok(fuelTypeService.addFuelType(dto, authorization));
    }

    @DeleteMapping("/{type}")
    public ResponseEntity<Void> deleteFuelTypeByName(@PathVariable String type, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        fuelTypeService.deleteFuelType(type, authorization);
        return ResponseEntity.noContent().build();
    }
}
