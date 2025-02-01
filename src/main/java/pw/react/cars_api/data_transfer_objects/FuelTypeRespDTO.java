package pw.react.cars_api.data_transfer_objects;

import pw.react.cars_api.models.FuelType;

public record FuelTypeRespDTO(Long id, String type) {
    public FuelTypeRespDTO(FuelType fuelType) {
        this(fuelType.getId(), fuelType.getType());
    }
}
