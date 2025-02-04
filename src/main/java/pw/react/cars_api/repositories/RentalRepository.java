package pw.react.cars_api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.cars_api.models.Car;
import pw.react.cars_api.models.Customer;
import pw.react.cars_api.models.Rental;

import java.util.Date;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, String> {
    boolean existsByCarAndStartAtBeforeAndEndAtAfter(Car car, Date startAtBefore, Date endAtAfter);
    Optional<Rental> findByCustomerAndId(Customer customer, String id);
    Page<Rental> findByCustomer(Customer customer, Pageable pageable);
    boolean existsByIdAndCustomer(String id, Customer customer);
}
