package pw.react.cars_api.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pw.react.cars_api.data_transfer_objects.CustomerDTO;
import pw.react.cars_api.services.CustomerService;

import java.net.URI;


@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/")
    public ResponseEntity<Void> createCustomer(@Valid @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            logger.error("Register Validation Error: {}",bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        try {
            String customerId = customerService.register(customerDTO);
            URI location = URI.create("/customer/" + customerId);
            logger.info("Customer registered with ID: {}", customerId);
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            logger.error("Register Error: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/external")
    public ResponseEntity<Void> createExternalCustomer(@Valid @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            logger.error("External Register Validation Error: {}",bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        try {
            String customerId = customerService.registerExternal(customerDTO);
            URI location = URI.create("/customer/" + customerId);
            logger.info("External Customer registered with ID: {}", customerId);
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            logger.error("External Register Error: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/login")
    public ResponseEntity<Void> loginCustomer(@Valid @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            logger.error("Login Validation Error: {}",bindingResult.getAllErrors());
        }
        try {
            String email = customerService.login(customerDTO);
            logger.info("Customer {} logged in", email);
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + email).build();
        } catch (Exception e) {
            logger.error("Login Error: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
