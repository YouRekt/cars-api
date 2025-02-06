package pw.react.cars_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import pw.react.cars_api.data_transfer_objects.ModelReqDTO;
import pw.react.cars_api.data_transfer_objects.ModelRespDTO;
import pw.react.cars_api.services.ModelService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/models")
public class ModelController {

    @Autowired
    private ModelService modelService;

    @GetMapping("/")
    @Operation(summary = "List all models.")
    public List<ModelRespDTO> getAllModels() {
        return modelService.getAllModels();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get model by id.")
    public ResponseEntity<ModelRespDTO> getModelById(@PathVariable String id) {
        return modelService.getModelById(id)
                           .map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    @Operation(summary = "Create new model.")
    public ResponseEntity<ModelRespDTO> createModel(@RequestBody ModelReqDTO dto, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        return ResponseEntity.ok(modelService.createModel(dto, authorization));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modify model data.")
    public ResponseEntity<ModelRespDTO> updateModel(@PathVariable String id, @RequestBody ModelReqDTO dto, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        return modelService.updateModel(id, dto, authorization)
                           .map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete model by id.")
    public ResponseEntity<Void> deleteModel(@PathVariable String id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        modelService.deleteModel(id, authorization);
        return ResponseEntity.noContent().build();
    }
}
