package pw.react.cars_api.repositories;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import pw.react.cars_api.models.Car;
import pw.react.cars_api.models.Location;
import pw.react.cars_api.models.Model;
import pw.react.cars_api.models.Rental;

import java.awt.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class CarSpecifications {
    public static Specification<Car> filterByModelAttributes(Optional<String> brandName, Optional<String> modelName,
                                                             Optional<Long> productionYear, Optional<String> fuelType,
                                                             Optional<Long> fuelCapacity, Optional<Long> seatCount,
                                                             Optional<Long> doorCount, Optional<BigDecimal> dailyRate,
                                                             Optional<String> locationCity,
                                                             Optional<BigDecimal> xpos, Optional<BigDecimal> ypos,
                                                             Optional<BigDecimal> distance,
                                                             Optional<String> from, Optional<String> to) {
        return (root, query, cb) -> {
            Join<Car, Model> model = root.join("model");
            Join<Car, Location> location = root.join("location");
            Predicate predicate = model.isNotNull();
            if (brandName.isPresent()) {
                predicate = cb.and(
                        predicate,
                        cb.or(
                                cb.like(cb.lower(model.get("brand").get("name")), "%" + brandName.get().toLowerCase() + "%"),
                                cb.like(cb.lower(model.get("brand").get("shortName")), "%" + brandName.get().toLowerCase() + "%")
                        )
                );
            }
            if (modelName.isPresent()) {
                predicate = cb.and(
                        predicate,
                        cb.like(cb.lower(model.get("name")), "%" + modelName.get().toLowerCase() + "%")
                );
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
            if (locationCity.isPresent()) {
                predicate = cb.and(predicate, cb.like(cb.lower(location.get("city")),
                        "%" + locationCity.get().toLowerCase() + "%"));
            }
            if (distance.isPresent() && xpos.isPresent() && ypos.isPresent()) {
                Expression<Double> distanceExpr = cb.function(
                        "ST_Distance_Sphere",
                        Double.class,
                        location.get("coordinates"),
                        cb.function("ST_GeomFromText", Point.class,
                                cb.literal(String.format("POINT(%f %f)", xpos.get(), ypos.get())))
                );
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(distanceExpr, distance.get().doubleValue()));
            }
            if (from.isPresent() && to.isPresent()) {
                assert query != null;
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Rental> rentalRoot = subquery.from(Rental.class);

                subquery.select(rentalRoot.get("car").get("id"))
                        .where(
                                cb.equal(rentalRoot.get("car").get("id"), root.get("id")),
                                cb.lessThanOrEqualTo(rentalRoot.get("startAt"), Timestamp.valueOf(to.get().replaceAll("[A-Z]"," ").strip())),
                                cb.greaterThanOrEqualTo(rentalRoot.get("endAt"), Timestamp.valueOf(from.get().replaceAll("[A-Z]"," ").strip()))
                        );
                predicate = cb.and(predicate, cb.not(cb.exists(subquery)));
            }
            return predicate;
        };
    }
}
