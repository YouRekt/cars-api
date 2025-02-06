package pw.react.cars_api.data_transfer_objects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import pw.react.cars_api.models.Customer;

public record CustomerRespDTO (
        String id,
        String email
) {
        public CustomerRespDTO(Customer customer) {
                this(customer.getId(),customer.getEmail());
        }
}