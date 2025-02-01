package pw.react.cars_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.cars_api.models.Location;

public interface LocationRepository extends JpaRepository<Location, String> {
}
