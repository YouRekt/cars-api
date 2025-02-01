package pw.react.cars_api.data_transfer_objects;

import jakarta.validation.constraints.NotBlank;

public record AdministratorDTO(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password
) {}
