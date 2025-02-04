package pw.react.cars_api.controllers;

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

    @GetMapping
    public List<ModelRespDTO> getAllModels() {
        return modelService.getAllModels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelRespDTO> getModelById(@PathVariable String id) {
        return modelService.getModelById(id)
                           .map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ModelRespDTO> createModel(@RequestBody ModelReqDTO dto, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        return ResponseEntity.ok(modelService.createModel(dto, authorization));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModelRespDTO> updateModel(@PathVariable String id, @RequestBody ModelReqDTO dto, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        return modelService.updateModel(id, dto, authorization)
                           .map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable String id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        modelService.deleteModel(id, authorization);
        return ResponseEntity.noContent().build();
    }
}
