package pw.react.cars_api.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pw.react.cars_api.data_transfer_objects.FuelTypeReqDTO;
import pw.react.cars_api.data_transfer_objects.FuelTypeRespDTO;
import pw.react.cars_api.models.FuelType;
import pw.react.cars_api.repositories.FuelTypeRepository;
import pw.react.cars_api.utils.Authorization;

import java.util.List;
import java.util.Optional;

@Service
public class FuelTypeService {

    private final FuelTypeRepository fuelTypeRepository;
    private final Authorization auth;
    public FuelTypeService(FuelTypeRepository fuelTypeRepository, Authorization auth) {
        this.fuelTypeRepository = fuelTypeRepository;
        this.auth = auth;
    }

    public List<FuelTypeRespDTO> getAllFuelTypes() {
        return fuelTypeRepository.findAll().stream()
                                 .map(FuelTypeRespDTO::new)
                                 .toList();
    }

    @Transactional
    public FuelTypeRespDTO addFuelType(FuelTypeReqDTO dto, String authToken) {
        auth.requireAdmin(authToken);
        FuelType fuelType = new FuelType();
        fuelType.setType(dto.type());
        return new FuelTypeRespDTO(fuelTypeRepository.save(fuelType));
    }

    @Transactional
    public void deleteFuelType(String type, String authToken) {
        auth.requireAdmin(authToken);
        Optional<FuelType> fuelType = fuelTypeRepository.findByType(type);
        fuelType.ifPresent(fuelTypeRepository::delete);
    }
}
