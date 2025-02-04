package pw.react.cars_api.repositories;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pw.react.cars_api.models.Car;
import pw.react.cars_api.models.Model;
import reactor.util.annotation.NonNull;

import java.math.BigDecimal;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, String>, JpaSpecificationExecutor<Car> {
}

