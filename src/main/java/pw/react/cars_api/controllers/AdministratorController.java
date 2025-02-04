package pw.react.cars_api.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pw.react.cars_api.data_transfer_objects.AdministratorDTO;
import pw.react.cars_api.models.Administrator;
import pw.react.cars_api.services.AdministratorService;

import java.net.URI;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/administrators")
public class AdministratorController {

    private final Logger logger = LoggerFactory.getLogger(AdministratorController.class);

    private final AdministratorService administratorService;

    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @PostMapping("/")
    public ResponseEntity<Void> registerAdministrator(@Valid @RequestBody AdministratorDTO administratorDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Register Validation Error: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        try {
            String administratorId = administratorService.registerAdministrator(administratorDTO);
            URI location = URI.create("/administrators/" + administratorId);
            logger.info("Administrator registered with ID: {}", administratorId);
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            logger.error("Register Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/")
    public ResponseEntity<Void> loginAdministrator(@Valid @RequestBody AdministratorDTO administratorDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Login Validation Error: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        try {
            String administrator = administratorService.loginAdministrator(administratorDTO);
            logger.info("Administrator {} logged in", administrator);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, "administrator-token=" + administrator + "; Path=/; Max-Age=86400").build();
        } catch (Exception e) {
            logger.error("Login Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getAdministrator(@PathVariable String id) {
        Optional<Administrator> administratorOptional = administratorService.getAdministratorById(id);
        logger.info("Administrator \"{}\" requested", administratorOptional.map(Administrator::getUsername).orElse("Administrator not found"));
        String response = administratorOptional.map(Administrator::getUsername).orElse("Administrator not found");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrator(@PathVariable String id) {
        try {
            administratorService.deleteAdministrator(id);
            logger.info("Administrator \"{}\" deleted", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Delete Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
