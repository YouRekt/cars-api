package pw.react.cars_api.data_transfer_objects;

import pw.react.cars_api.models.Image;

public record ImageRespDTO (
        String id,
        String url
) {
    public ImageRespDTO(Image image) {
        this(
                image.getId(),
                System.getenv("API_URI") + "/images/" + image.getId()
        );
    }
}
