package pw.react.cars_api.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.GeometryFactory;
import pw.react.cars_api.data_transfer_objects.LocationReqDTO;
import pw.react.cars_api.data_transfer_objects.LocationRespDTO;
import pw.react.cars_api.models.Location;
import pw.react.cars_api.repositories.LocationRepository;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationRespDTO> getAllLocations() {
        return locationRepository.findAll().stream()
                                 .map(LocationRespDTO::new)
                                 .toList();
    }

    public Optional<LocationRespDTO> getLocationById(String id) {
        return locationRepository.findById(id).map(LocationRespDTO::new);
    }

    @Transactional
    public LocationRespDTO createLocation(LocationReqDTO dto) {
        Point coordinates = geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(dto.latitude(), dto.longitude()));

        Location location = new Location();
        return getLocationRespDTO(dto, coordinates, location);
    }

    private LocationRespDTO getLocationRespDTO(LocationReqDTO dto, Point coordinates, Location location) {
        location.setCountry(dto.country());
        location.setCity(dto.city());
        location.setPostalCode(dto.postalCode());
        location.setStreet(dto.street());
        location.setHouseNumber(dto.houseNumber());
        location.setCoordinates(coordinates);

        return new LocationRespDTO(locationRepository.save(location));
    }

    @Transactional
    public Optional<LocationRespDTO> updateLocation(String id, LocationReqDTO dto) {
        return locationRepository.findById(id).map(location -> {
            Point coordinates = geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(dto.latitude(), dto.longitude()));

            return getLocationRespDTO(dto, coordinates, location);
        });
    }

    @Transactional
    public void deleteLocation(String id) {
        if (!locationRepository.existsById(id)) {
            throw new RuntimeException("Location not found");
        }
        locationRepository.deleteById(id);
    }
}
