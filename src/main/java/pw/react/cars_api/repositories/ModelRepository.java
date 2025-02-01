package pw.react.cars_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.cars_api.models.Model;

public interface ModelRepository extends JpaRepository<Model, String> {
}
