package pw.react.cars_api.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import pw.react.cars_api.data_transfer_objects.LocationReqDTO;
import pw.react.cars_api.data_transfer_objects.LocationRespDTO;
import pw.react.cars_api.services.LocationService;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public List<LocationRespDTO> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationRespDTO> getLocationById(@PathVariable String id) {
        return locationService.getLocationById(id)
                              .map(ResponseEntity::ok)
                              .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LocationRespDTO> createLocation(@RequestBody LocationReqDTO dto) {
        return ResponseEntity.ok(locationService.createLocation(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationRespDTO> updateLocation(@PathVariable String id, @RequestBody LocationReqDTO dto) {
        return locationService.updateLocation(id, dto)
                              .map(ResponseEntity::ok)
                              .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable String id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
