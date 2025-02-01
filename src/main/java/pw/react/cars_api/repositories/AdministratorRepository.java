package pw.react.cars_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.cars_api.models.Administrator;

import java.util.List;

public interface AdministratorRepository extends JpaRepository<Administrator, String> {
    boolean existsByEmailOrUsername(String email, String username);
    List<Administrator> findByEmailOrUsername(String email, String username);
}
