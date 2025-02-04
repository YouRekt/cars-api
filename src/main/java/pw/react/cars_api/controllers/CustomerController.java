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
import pw.react.cars_api.data_transfer_objects.CustomerDTO;
import pw.react.cars_api.models.Customer;
import pw.react.cars_api.services.CustomerService;
import pw.react.cars_api.utils.UnauthorizedRequestException;

import java.net.URI;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/")
    public ResponseEntity<Void> createCustomer(@Valid @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Register Validation Error: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        try {
            String customerId = customerService.register(customerDTO);
            URI location = URI.create("/customers/" + customerId);
            logger.info("Customer registered with ID: {}", customerId);
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            logger.error("Register Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/external")
    public ResponseEntity<Void> createExternalCustomer(@Valid @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("External Register Validation Error: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        try {
            String customerId = customerService.registerExternal(customerDTO);
            URI location = URI.create("/customers/" + customerId);
            logger.info("External Customer registered with ID: {}", customerId);
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            logger.error("External Register Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/login")
    public ResponseEntity<Void> loginCustomer(@Valid @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Login Validation Error: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        try {
            String email = customerService.login(customerDTO);
            logger.info("Customer {} logged in", email);
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + email).build();
        } catch (Exception e) {
            logger.error("Login Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<Page<Customer>> getAllCustomers(@RequestParam("page") int page, @RequestParam("size") int size, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            Page<Customer> customerPage = customerService.getAllCustomers(page, size, authorization);
            logger.info("Customers page {} requested", page);
            return ResponseEntity.ok(customerPage);
        } catch (UnauthorizedRequestException e) {
            logger.error("Unauthorized access {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Error getting customers {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editCustomer(@Valid @RequestBody CustomerDTO customerDTO, @PathVariable String id, BindingResult bindingResult, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        if (bindingResult.hasErrors()) {
            logger.error("Edit Customer Validation Error: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        try {
            customerService.editCustomer(customerDTO,id,authorization);
            URI location = URI.create("/customers/" + id);
            logger.info("Customer edited with ID: {}", id);
            return ResponseEntity.created(location).build();
        } catch (UnauthorizedRequestException e) {
            logger.error("Unauthorized edit customer request {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Error editing customer {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") String id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            customerService.deleteCustomer(id, authorization);
            logger.info("Customer {} deleted", id);
            return ResponseEntity.ok().build();
        } catch (UnauthorizedRequestException e) {
            logger.error("Unauthorized delete request {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Error deleting customer {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
