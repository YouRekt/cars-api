package pw.react.cars_api.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.cars_api.data_transfer_objects.RentalDTO;
import pw.react.cars_api.models.Rental;
import pw.react.cars_api.services.RentalService;
import pw.react.cars_api.utils.UnauthorizedRequestException;

import java.net.URI;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    private final Logger logger = LoggerFactory.getLogger(RentalController.class);

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping("/")
    public ResponseEntity<Void> createRental(@Valid @RequestBody RentalDTO rentalDTO, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            Rental rental = rentalService.createRental(rentalDTO, authorization);
            URI location = URI.create("/rentals/" + rental.getId());
            logger.info("Created rental {}", rentalDTO);
            return ResponseEntity.created(location).build();
        } catch (UnauthorizedRequestException e) {
            logger.error("Unauthorized create rental request {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Error creating rental {}", rentalDTO);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable("id") String id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            Rental rental = rentalService.getRental(id, authorization);
            logger.info("Rental with id {} returned", id);
            return ResponseEntity.ok(rental);
        } catch (UnauthorizedRequestException e) {
            logger.error("Unauthorized get rental request {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Error getting rental with id {}", id );
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<Page<Rental>> getAllRentals(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization, @RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            Page<Rental> rentalPage = rentalService.getRentals(authorization,page,size);
            logger.info("Rental list returned {}", rentalPage);
            return ResponseEntity.ok(rentalPage);
        } catch (UnauthorizedRequestException e) {
            logger.error("Unauthorized get rentals request {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Error getting rental list {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
