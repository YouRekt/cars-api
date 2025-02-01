package pw.react.cars_api.data_transfer_objects;

import pw.react.cars_api.models.Car;

public record CarRespDTO(
        String id,
        ModelRespDTO model,
        LocationRespDTO location,
        String imageUrl
) {
        public CarRespDTO(Car car) {
                this(
                        car.getId(),
                        new ModelRespDTO(car.getModel()),
                        new LocationRespDTO(car.getLocation()),
                        car.getImage() != null ? "/images/" + car.getImage().getId() : null
                );
        }
}
