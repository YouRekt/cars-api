package pw.react.cars_api.controllers;

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

    @GetMapping
    public List<BrandRespDTO> getAllBrands() {
        return brandService.getAllBrands();
    }

    @PostMapping
    public ResponseEntity<BrandRespDTO> addBrand(@RequestBody BrandReqDTO dto) {
        return ResponseEntity.ok(brandService.addBrand(dto));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteBrandByName(@PathVariable String name) {
        brandService.deleteBrandByName(name);
        return ResponseEntity.noContent().build();
    }
}
