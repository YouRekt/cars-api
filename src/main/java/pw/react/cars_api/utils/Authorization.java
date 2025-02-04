package pw.react.cars_api.utils;

import pw.react.cars_api.models.Customer;
import pw.react.cars_api.repositories.AdministratorRepository;
import pw.react.cars_api.repositories.CustomerRepository;

import org.springframework.stereotype.Service;

@Service
public class Authorization {
    private final CustomerRepository customerRepository;
    private final AdministratorRepository administratorRepository;

    public Authorization(CustomerRepository customerRepository, AdministratorRepository administratorRepository) {
        this.customerRepository = customerRepository;
        this.administratorRepository = administratorRepository;
    }

    public Customer authorize(String authorizationHeader) {
        String customerEmail = authorizationHeader.split(" ")[1];

        return customerRepository.findByEmail(customerEmail).orElseThrow(() -> new UnauthorizedRequestException("Customer not found"));
    }

    public void requireAdmin(String authorizationHeader) {
        String adminId = authorizationHeader.strip().split(" ")[1];
        if (administratorRepository.findById(adminId).isEmpty()) {
            throw new UnauthorizedRequestException("Only admins are allowed to perform this operation.");
        }
    }

    public boolean isAdmin(String authorizationHeader) {
        String adminId = authorizationHeader.split(" ")[1];

        return administratorRepository.findById(adminId).isPresent();
    }
}

