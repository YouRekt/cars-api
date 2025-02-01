package pw.react.cars_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.cars_api.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
