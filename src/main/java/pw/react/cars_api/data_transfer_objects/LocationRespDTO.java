package pw.react.cars_api.data_transfer_objects;

import pw.react.cars_api.models.Location;

public record LocationRespDTO(
    String id,
    String fullAddress,
    String city,
    double latitude,
    double longitude
) {
    public LocationRespDTO(Location location) {
        this(
            location.getId(),
            location.getFullAddress(),
            location.getCity(),
            location.getCoordinates().getX(),
            location.getCoordinates().getY()
        );
    }
}
