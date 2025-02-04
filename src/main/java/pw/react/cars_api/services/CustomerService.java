package pw.react.cars_api.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pw.react.cars_api.data_transfer_objects.CustomerDTO;
import pw.react.cars_api.models.Customer;
import pw.react.cars_api.models.Rental;
import pw.react.cars_api.repositories.CustomerRepository;
import pw.react.cars_api.repositories.RentalRepository;
import pw.react.cars_api.utils.Authorization;
import pw.react.cars_api.utils.UnauthorizedRequestException;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RentalRepository rentalRepository;
    private final Authorization auth;

    public CustomerService(CustomerRepository customerRepository, RentalRepository rentalRepository, Authorization auth) {
        this.customerRepository = customerRepository;
        this.rentalRepository = rentalRepository;
        this.auth = auth;
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

    @Transactional
    public void editCustomer(CustomerDTO customerDTO, String id, String authorization) {
        if(auth.isAdmin(authorization)) {
            Optional<Customer> customer = customerRepository.findById(id);

            if (customer.isEmpty()) {
                throw new IllegalArgumentException("Customer not found");
            }

            Customer customerToEdit = customer.get();
            customerToEdit.setEmail(customerDTO.email());
            customerRepository.save(customerToEdit);

            return;
        }

        auth.authorize(authorization);

        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }

        Customer customerToEdit = customer.get();
        customerToEdit.setEmail(customerDTO.email());
        customerRepository.save(customerToEdit);
    }

    @Transactional
    public void deleteCustomer(String customerId, String authorization) {
        if (!auth.isAdmin(authorization)) {
            throw new UnauthorizedRequestException("Unauthorized access");
        }

        Optional<Customer> customer = customerRepository.findById(customerId);

        if(customer.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }

        List<Rental> customerRentals = rentalRepository.findAllByCustomer(customer.get());

        customerRentals.forEach(rental -> rentalRepository.deleteById(rental.getId()));

        customerRepository.deleteById(customerId);
    }
}
