package pw.react.cars_api.data_transfer_objects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AdministratorDTO(
        @Email(message = "Invalid email format")
        String email,

        String username,

        @NotBlank(message = "Password is required")
        String password
) {}
