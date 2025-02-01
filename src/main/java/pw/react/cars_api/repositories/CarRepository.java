package pw.react.cars_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.cars_api.models.Car;

public interface CarRepository extends JpaRepository<Car, String> {
}
