package pw.react.cars_api.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pw.react.cars_api.data_transfer_objects.RentalDTO;
import pw.react.cars_api.models.Car;
import pw.react.cars_api.models.Customer;
import pw.react.cars_api.models.Rental;
import pw.react.cars_api.repositories.CarRepository;
import pw.react.cars_api.repositories.CustomerRepository;
import pw.react.cars_api.repositories.RentalRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;

    public RentalService(RentalRepository rentalRepository, CarRepository carRepository, CustomerRepository customerRepository) {
        this.rentalRepository = rentalRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
    }

    private Customer authorize(String authorization) {
        String customerEmail = authorization.split(" ")[1];

        Optional<Customer> customer = customerRepository.findByEmail(customerEmail);

        if(customer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        return customer.get();
    }

    @Transactional
    public Rental createRental(RentalDTO rentalDTO, String authorization) {
        Customer customer = authorize(authorization);

        Optional<Car> car =  carRepository.findById(rentalDTO.carId());

        if(car.isEmpty()) {
            throw new IllegalArgumentException("Car not found");
        }

        if(rentalRepository.existsByCarAndStartAtBeforeAndEndAtAfter(car.get(),rentalDTO.endAt(),rentalDTO.startAt())) {
            throw new IllegalArgumentException("Rental for this Car already exists within the provided date range");
        }

        Rental rental = new Rental();
        rental.setCar(car.get());
        rental.setCustomer(customer);
        rental.setStartAt(rentalDTO.startAt());
        rental.setEndAt(rentalDTO.endAt());
        rental.setCancelled(false);

        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental getRental(String id, String authorization) {
        Customer customer = authorize(authorization);

        Optional<Rental> rental = rentalRepository.findByCustomerAndId(customer, id);
        if(rental.isEmpty()) {
            throw new IllegalArgumentException("Rental belonging to the customer not found");
        }

        return rental.get();
    }

    @Transactional
    public Page<Rental> getRentals(String authorization, int pageNumber, int pageSize) {
        String customerEmail = authorization.split(" ")[1];
        Optional<Customer> customer = customerRepository.findByEmail(customerEmail);
        if(customer.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }

        return rentalRepository.findByCustomer(customer.get(), PageRequest.of(pageNumber, pageSize));
    }

}
