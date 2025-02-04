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
import pw.react.cars_api.repositories.RentalRepository;
import pw.react.cars_api.utils.Authorization;

import java.util.Optional;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;

    private final Authorization auth;

    public RentalService(RentalRepository rentalRepository, CarRepository carRepository, Authorization auth) {
        this.rentalRepository = rentalRepository;
        this.carRepository = carRepository;
        this.auth = auth;
    }

    @Transactional
    public Rental createRental(RentalDTO rentalDTO, String authorization) {
        Customer customer = auth.authorize(authorization);

        Optional<Car> car = carRepository.findById(rentalDTO.carId());

        if (car.isEmpty()) {
            throw new IllegalArgumentException("Car not found");
        }

        if (rentalRepository.existsByCarAndStartAtBeforeAndEndAtAfter(car.get(), rentalDTO.endAt(), rentalDTO.startAt())) {
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
        if (auth.isAdmin(authorization)) {
            return rentalRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Rental does not exist"));
        }

        Customer customer = auth.authorize(authorization);

        Optional<Rental> rental = rentalRepository.findByCustomerAndId(customer, id);
        if (rental.isEmpty()) {
            throw new IllegalArgumentException("Rental belonging to the customer not found");
        }

        return rental.get();
    }

    @Transactional
    public Page<Rental> getRentals(String authorization, int pageNumber, int pageSize) {
        if (auth.isAdmin(authorization)) {
            return rentalRepository.findAll(PageRequest.of(pageNumber, pageSize));
        }

        Customer customer = auth.authorize(authorization);

        return rentalRepository.findByCustomer(customer, PageRequest.of(pageNumber, pageSize));
    }

    @Transactional
    public void deleteRental(String id, String authorization) {
        if (auth.isAdmin(authorization)) {
            if (!rentalRepository.existsById(id)) {
                throw new IllegalArgumentException("Rental does not exist");
            }

            rentalRepository.deleteById(id);
            return;
        }

        Customer customer = auth.authorize(authorization);

        if (!rentalRepository.existsByIdAndCustomer(id, customer)) {
            throw new IllegalArgumentException("Rental not found");
        }

        rentalRepository.deleteById(id);
    }
}
