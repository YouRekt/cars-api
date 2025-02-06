package pw.react.cars_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import pw.react.cars_api.data_transfer_objects.BrandReqDTO;
import pw.react.cars_api.data_transfer_objects.BrandRespDTO;
import pw.react.cars_api.services.BrandService;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/")
    @Operation(summary = "List all brands")
    public List<BrandRespDTO> getAllBrands() {
        return brandService.getAllBrands();
    }

    @PostMapping("/")
    @Operation(summary = "Create new brand")
    public ResponseEntity<BrandRespDTO> addBrand(@RequestBody BrandReqDTO dto, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        return ResponseEntity.ok(brandService.addBrand(dto,authorization));
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Delete brand by name")
    public ResponseEntity<Void> deleteBrandByName(@PathVariable String name, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        brandService.deleteBrandByName(name,authorization);
        return ResponseEntity.noContent().build();
    }
}
