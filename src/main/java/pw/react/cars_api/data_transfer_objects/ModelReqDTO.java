package pw.react.cars_api.data_transfer_objects;

import java.math.BigDecimal;

public record ModelReqDTO(
    Long brandId,
    String name,
    Long productionYear,
    Long fuelTypeId,
    Long fuelCapacity,
    Long seatCount,
    Long doorCount,
    BigDecimal dailyRate
) {}
