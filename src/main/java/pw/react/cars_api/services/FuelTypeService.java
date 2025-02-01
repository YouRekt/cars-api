package pw.react.cars_api.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pw.react.cars_api.data_transfer_objects.FuelTypeReqDTO;
import pw.react.cars_api.data_transfer_objects.FuelTypeRespDTO;
import pw.react.cars_api.models.FuelType;
import pw.react.cars_api.repositories.FuelTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FuelTypeService {

    private final FuelTypeRepository fuelTypeRepository;

    public FuelTypeService(FuelTypeRepository fuelTypeRepository) {
        this.fuelTypeRepository = fuelTypeRepository;
    }

    public List<FuelTypeRespDTO> getAllFuelTypes() {
        return fuelTypeRepository.findAll().stream()
                                 .map(FuelTypeRespDTO::new)
                                 .toList();
    }

    @Transactional
    public FuelTypeRespDTO addFuelType(FuelTypeReqDTO dto) {
        FuelType fuelType = new FuelType();
        fuelType.setType(dto.type());
        return new FuelTypeRespDTO(fuelTypeRepository.save(fuelType));
    }

    @Transactional
    public void deleteFuelType(String type) {
        Optional<FuelType> fuelType = fuelTypeRepository.findByType(type);
        fuelType.ifPresent(fuelTypeRepository::delete);
    }
}
