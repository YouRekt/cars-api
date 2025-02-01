package pw.react.cars_api.controllers;

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
    public ResponseEntity<ModelRespDTO> createModel(@RequestBody ModelReqDTO dto) {
        return ResponseEntity.ok(modelService.createModel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModelRespDTO> updateModel(@PathVariable String id, @RequestBody ModelReqDTO dto) {
        return modelService.updateModel(id, dto)
                           .map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable String id) {
        modelService.deleteModel(id);
        return ResponseEntity.noContent().build();
    }
}
