package pw.react.cars_api.data_transfer_objects;

import java.util.Date;

public record RentalDTO(
   String carId,
   Date startAt,
   Date endAt
) {}
