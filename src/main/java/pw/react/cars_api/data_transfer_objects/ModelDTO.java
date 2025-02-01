package pw.react.cars_api.data_transfer_objects;

import pw.react.cars_api.models.Model;

import java.math.BigDecimal;

public record ModelDTO(
    String id,
    String brandName,
    String name,
    Long productionYear,
    String fuelType,
    Long fuelCapacity,
    Long seatCount,
    Long doorCount,
    BigDecimal dailyRate
) {
    public ModelDTO(Model model) {
        this(
            model.getId(),
            model.getBrand().getName(),
            model.getName(),
            model.getProductionYear(),
            model.getFuelType().getType(),
            model.getFuelCapacity(),
            model.getSeatCount(),
            model.getDoorCount(),
            model.getDailyRate()
        );
    }
}
