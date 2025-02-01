package pw.react.cars_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.cars_api.models.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    boolean existsByEmail(String email);
    Optional<Customer> findByEmail(String email);
}
