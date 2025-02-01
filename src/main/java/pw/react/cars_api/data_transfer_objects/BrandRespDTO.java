package pw.react.cars_api.data_transfer_objects;

import pw.react.cars_api.models.Brand;

public record BrandRespDTO(Long id, String name, String shortName) {
    public BrandRespDTO(Brand brand) {
        this(brand.getId(), brand.getName(), brand.getShortName());
    }
}
