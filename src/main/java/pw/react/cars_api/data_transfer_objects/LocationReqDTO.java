package pw.react.cars_api.data_transfer_objects;

public record LocationReqDTO(
    String country,
    String city,
    String postalCode,
    String street,
    Long houseNumber,
    double latitude,
    double longitude
) {}
