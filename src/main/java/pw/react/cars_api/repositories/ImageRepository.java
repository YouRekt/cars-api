package pw.react.cars_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.cars_api.models.Image;

public interface ImageRepository extends JpaRepository<Image, String> {
}
