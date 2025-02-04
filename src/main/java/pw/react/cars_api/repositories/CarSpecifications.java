package pw.react.cars_api.repositories;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import pw.react.cars_api.models.Car;
import pw.react.cars_api.models.Model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class CarSpecifications {
    public static Specification<Car> filterByModelAttributes(Optional<String> brandName, Optional<String> modelName,
                                                             Optional<Long> productionYear, Optional<String> fuelType,
                                                             Optional<Long> fuelCapacity, Optional<Long> seatCount,
                                                             Optional<Long> doorCount, Optional<BigDecimal> dailyRate) {
        return (root, query, cb) -> {
            Join<Car, Model> model = root.join("model");
            Predicate predicate = model.isNotNull();
            if (brandName.isPresent()) {
                predicate = cb.and(predicate, cb.or(cb.equal(model.get("brand").get("name"), brandName.get()),
                        cb.equal(model.get("brand").get("shortName"), brandName.get())));
            }
            if (modelName.isPresent()) {
                predicate = cb.and(predicate, cb.equal(model.get("name"), modelName.get()));
            }
            if (productionYear.isPresent()) {
                predicate = cb.and(predicate, cb.equal(model.get("productionYear"), productionYear.get()));
            }

            if (fuelType.isPresent()) {
                predicate = cb.and(predicate, cb.equal(model.get("fuelType").get("type"), fuelType.get()));
            }
            if (fuelCapacity.isPresent()) {
                predicate = cb.and(predicate, cb.equal(model.get("fuelCapacity"), fuelCapacity.get()));
            }
            if (seatCount.isPresent()) {
                predicate = cb.and(predicate, cb.equal(model.get("seatCount"), seatCount.get()));
            }
            if (doorCount.isPresent()) {
                predicate = cb.and(predicate, cb.equal(model.get("doorCount"), doorCount.get()));
            }
            if (dailyRate.isPresent()) {
                predicate = cb.and(predicate, cb.le(model.get("dailyRate"), dailyRate.get()));
            }
            return predicate;
        };
    }
}
