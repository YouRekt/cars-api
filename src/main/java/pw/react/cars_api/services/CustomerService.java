package pw.react.cars_api.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pw.react.cars_api.data_transfer_objects.CustomerDTO;
import pw.react.cars_api.models.Customer;
import pw.react.cars_api.repositories.CustomerRepository;
import pw.react.cars_api.utils.Authorization;
import pw.react.cars_api.utils.UnauthorizedRequestException;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final Authorization auth;

    public CustomerService(CustomerRepository customerRepository, Authorization authorization) {
        this.customerRepository = customerRepository;
        this.auth = authorization;
    }

    @Transactional
    public String register(CustomerDTO customerDTO) {

        if (customerRepository.existsByEmail(customerDTO.email())) {
            throw new IllegalArgumentException("Customer already exists");
        }

        Customer customer = new Customer();
        customer.setEmail(customerDTO.email());
        customer.setExternal(false);

        customerRepository.save(customer);

        return customer.getId();
    }

    @Transactional
    public String registerExternal(CustomerDTO customerDTO) {

        if (customerRepository.existsByEmail(customerDTO.email())) {
            throw new IllegalArgumentException("Customer already exists");
        }

        Customer customer = new Customer();
        customer.setEmail(customerDTO.email());
        customer.setExternal(true);

        customerRepository.save(customer);

        return customer.getId();
    }

    @Transactional
    public String login(CustomerDTO customerDTO) {
        Optional<Customer> customer = customerRepository.findByEmail(customerDTO.email());

        if (customer.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }

        return customer.get().getEmail();
    }

    public Page<Customer> getAllCustomers(int page, int size, String authorization) {
        if (!auth.isAdmin(authorization)) {
            throw new UnauthorizedRequestException("Unauthorized access");
        }

        return customerRepository.findAll(PageRequest.of(page, size));
    }
}
