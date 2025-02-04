package pw.react.cars_api.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pw.react.cars_api.data_transfer_objects.AdministratorDTO;
import pw.react.cars_api.models.Administrator;
import pw.react.cars_api.services.AdministratorService;
import pw.react.cars_api.utils.UnauthorizedRequestException;

import java.net.URI;

@RestController
@RequestMapping("/administrators")
public class AdministratorController {

    private final Logger logger = LoggerFactory.getLogger(AdministratorController.class);

    private final AdministratorService administratorService;

    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @PostMapping("/")
    public ResponseEntity<Void> registerAdministrator(@Valid @RequestBody AdministratorDTO administratorDTO, BindingResult bindingResult, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (bindingResult.hasErrors()) {
            logger.error("Register Validation Error: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        try {
            String administratorId = administratorService.registerAdministrator(administratorDTO, authorization);
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
    public ResponseEntity<String> getAdministrator(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            Administrator administrator = administratorService.getAdministratorById(id, authorization);
            logger.info("Administrator \"{}\" requested", administrator);
            return ResponseEntity.ok().body(administrator.getUsername());

        } catch (UnauthorizedRequestException e) {
            logger.error("Unauthorized get admin Request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Get Administrator Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<Page<Administrator>> getAllAdministrators(@RequestParam("page") int page, @RequestParam("size") int size, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            Page<Administrator> administratorPage = administratorService.getAllAdministrators(authorization, page, size);
            logger.info("Administrators list page {} requested", page);
            return ResponseEntity.ok().body(administratorPage);
        } catch (UnauthorizedRequestException e) {
            logger.error("Unauthorized get all admins Request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Get All Administrators Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrator(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            administratorService.deleteAdministrator(id, authorization);
            logger.info("Administrator \"{}\" deleted", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Delete Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
