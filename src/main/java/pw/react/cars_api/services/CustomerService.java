package pw.react.cars_api.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pw.react.cars_api.data_transfer_objects.CustomerDTO;
import pw.react.cars_api.models.Customer;
import pw.react.cars_api.repositories.CustomerRepository;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public String register(CustomerDTO customerDTO) {

        if(customerRepository.existsByEmail(customerDTO.email())) {
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

        if(customerRepository.existsByEmail(customerDTO.email())) {
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

        if(customer.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }

        return customer.get().getEmail();
    }
}
