package pw.react.cars_api.data_transfer_objects;

import org.springframework.beans.factory.annotation.Value;
import pw.react.cars_api.models.Car;

public record CarRespDTO(
        String id,
        ModelRespDTO model,
        LocationRespDTO location,
        String imageId,
        String imageUrl
) {

        public CarRespDTO(Car car) {
                this(
                        car.getId(),
                        new ModelRespDTO(car.getModel()),
                        new LocationRespDTO(car.getLocation()),
                        car.getImage() != null ? car.getImage().getId() : null,
                        car.getImage() != null ? System.getenv("API_URI") + "/images/" + car.getImage().getId() : null
                );
        }
}
