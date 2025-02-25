package pw.react.cars_api.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pw.react.cars_api.data_transfer_objects.AdministratorDTO;
import pw.react.cars_api.models.Administrator;
import pw.react.cars_api.repositories.AdministratorRepository;
import pw.react.cars_api.utils.Authorization;
import pw.react.cars_api.utils.UnauthorizedRequestException;

import java.util.Optional;

@Service
public class AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final Authorization auth;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AdministratorService(AdministratorRepository administratorRepository, Authorization auth) {
        this.administratorRepository = administratorRepository;
        this.auth = auth;
    }

    @Transactional
    public String registerAdministrator(AdministratorDTO administratorDTO, String authorization) {
        if(!auth.isAdmin(authorization)) {
            throw new UnauthorizedRequestException("Unauthorized access");
        }

        if (administratorRepository.existsByUsername(administratorDTO.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(administratorDTO.password());

        Administrator administrator = new Administrator();
        administrator.setUsername(administratorDTO.username());
        administrator.setPasswordHash(hashedPassword);

        administratorRepository.save(administrator);

        return administrator.getId();
    }

    @Transactional
    public String loginAdministrator(AdministratorDTO administratorDTO) {

        Optional<Administrator> administrator = administratorRepository.findByUsername(administratorDTO.username());

        if (administrator.isEmpty()) {
            throw new IllegalArgumentException("Username already exists");
        }


        if (!passwordEncoder.matches(administratorDTO.password(), administrator.get().getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return administrator.get().getId();
    }

    public Administrator getAdministratorById(String id, String authorization) {
        if(!auth.isAdmin(authorization)) {
            throw new UnauthorizedRequestException("Unauthorized access");
        }

        return administratorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Admin not found"));
    }

    public Page<Administrator> getAllAdministrators(String authorization, int page, int size) {
        if(!auth.isAdmin(authorization)) {
            throw new UnauthorizedRequestException("Unauthorized access");
        }

        return administratorRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    public void deleteAdministrator(String id, String authorization) {
        if(!auth.isAdmin(authorization)) {
            throw new UnauthorizedRequestException("Unauthorized access");
        }

        if (!administratorRepository.existsById(id)) {
            throw new IllegalArgumentException("Administrator not found");
        }

        administratorRepository.deleteById(id);
    }
}
