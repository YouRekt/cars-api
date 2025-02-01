package pw.react.cars_api.services;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pw.react.cars_api.data_transfer_objects.AdministratorDTO;
import pw.react.cars_api.models.Administrator;
import pw.react.cars_api.repositories.AdministratorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AdministratorService {

    private final AdministratorRepository administratorRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AdministratorService(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    @Transactional
    public String registerAdministrator(AdministratorDTO administratorDTO) {

        if (administratorRepository.existsByEmailOrUsername(administratorDTO.email(), administratorDTO.username())) {
            throw new IllegalArgumentException("Email or username already exists");
        }

        String hashedPassword = passwordEncoder.encode(administratorDTO.password());

        Administrator administrator = new Administrator();
        administrator.setUsername(administratorDTO.username());
        administrator.setEmail(administratorDTO.email());
        administrator.setPasswordHash(hashedPassword);

        administratorRepository.save(administrator);

        return administrator.getId();
    }

    public String loginAdministrator(AdministratorDTO administratorDTO) {

        List<Administrator> administrators = administratorRepository.findByEmailOrUsername(administratorDTO.email(), administratorDTO.username());

        if (administrators.isEmpty()) {
            throw new IllegalArgumentException("Email or username already exists");
        }

        Administrator administrator = administrators.getFirst();

        if(!passwordEncoder.matches(administratorDTO.password(), administrator.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return administrator.getId();
    }

    public Optional<Administrator> getAdministratorById(String id) {
        return administratorRepository.findById(id);
    }
}
