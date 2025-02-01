package pw.react.cars_api.data_transfer_objects;

import pw.react.cars_api.models.Location;

public record LocationDTO(
    String id,
    String fullAddress,
    double latitude,
    double longitude
) {
    public LocationDTO(Location location) {
        this(
            location.getId(),
            location.getFullAddress(),
            location.getCoordinates().getX(),
            location.getCoordinates().getY()
        );
    }
}
