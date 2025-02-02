package pw.react.cars_api.utils;

import pw.react.cars_api.models.Customer;
import pw.react.cars_api.repositories.CustomerRepository;

import org.springframework.stereotype.Service;

@Service
public class Authorization {
    private final CustomerRepository customerRepository;

    public Authorization(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer authorize(String authorizationHeader) {
        String customerEmail = authorizationHeader.split(" ")[1];

        return customerRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new UnauthorizedRequestException("Customer not found"));
    }
}

