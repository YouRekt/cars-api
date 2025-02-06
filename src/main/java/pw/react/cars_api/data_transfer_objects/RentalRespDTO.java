package pw.react.cars_api.data_transfer_objects;

import pw.react.cars_api.models.Rental;

import java.util.Date;

public record RentalRespDTO (
    String id,
    CarRespDTO car,
    CustomerRespDTO customer,
    Date startAt,
    Date endAt,
    boolean isCancelled
) {
    public RentalRespDTO(Rental rental) {
        this(rental.getId(),
                new CarRespDTO(rental.getCar()),
                new CustomerRespDTO(rental.getCustomer()),
                rental.getStartAt(),
                rental.getEndAt(),
                rental.isCancelled());
    }
}
