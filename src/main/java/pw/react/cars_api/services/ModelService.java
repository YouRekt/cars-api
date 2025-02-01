package pw.react.cars_api.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pw.react.cars_api.data_transfer_objects.ModelReqDTO;
import pw.react.cars_api.data_transfer_objects.ModelRespDTO;
import pw.react.cars_api.models.Brand;
import pw.react.cars_api.models.FuelType;
import pw.react.cars_api.models.Model;
import pw.react.cars_api.repositories.BrandRepository;
import pw.react.cars_api.repositories.FuelTypeRepository;
import pw.react.cars_api.repositories.ModelRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModelService {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private FuelTypeRepository fuelTypeRepository;

    public List<ModelRespDTO> getAllModels() {
        return modelRepository.findAll().stream()
                              .map(ModelRespDTO::new)
                              .collect(Collectors.toList());
    }

    public Optional<ModelRespDTO> getModelById(String id) {
        return modelRepository.findById(id).map(ModelRespDTO::new);
    }

    @Transactional
    public ModelRespDTO createModel(ModelReqDTO dto) {
        Brand brand = brandRepository.findById(dto.brandId())
                                     .orElseThrow(() -> new RuntimeException("Brand not found"));
        FuelType fuelType = fuelTypeRepository.findById(dto.fuelTypeId())
                                              .orElseThrow(() -> new RuntimeException("Fuel Type not found"));

        Model model = new Model();
        return getModelRespDTO(dto, brand, fuelType, model);
    }

    private ModelRespDTO getModelRespDTO(ModelReqDTO dto, Brand brand, FuelType fuelType, Model model) {
        model.setBrand(brand);
        model.setName(dto.name());
        model.setProductionYear(dto.productionYear());
        model.setFuelType(fuelType);
        model.setFuelCapacity(dto.fuelCapacity());
        model.setSeatCount(dto.seatCount());
        model.setDoorCount(dto.doorCount());
        model.setDailyRate(dto.dailyRate());

        return new ModelRespDTO(modelRepository.save(model));
    }

    @Transactional
    public Optional<ModelRespDTO> updateModel(String id, ModelReqDTO dto) {
        return modelRepository.findById(id).map(model -> {
            Brand brand = brandRepository.findById(dto.brandId())
                                         .orElseThrow(() -> new RuntimeException("Brand not found"));
            FuelType fuelType = fuelTypeRepository.findById(dto.fuelTypeId())
                                                  .orElseThrow(() -> new RuntimeException("Fuel Type not found"));

            return getModelRespDTO(dto, brand, fuelType, model);
        });
    }

    @Transactional
    public void deleteModel(String id) {
        if (!modelRepository.existsById(id)) {
            throw new RuntimeException("Model not found");
        }
        modelRepository.deleteById(id);
    }
}
