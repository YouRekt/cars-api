package pw.react.cars_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.cars_api.models.Administrator;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, String> {
    boolean existsByUsername(String username);
    Optional<Administrator> findByUsername(String username);
}
