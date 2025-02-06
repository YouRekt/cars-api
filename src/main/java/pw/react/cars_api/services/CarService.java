package pw.react.cars_api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pw.react.cars_api.data_transfer_objects.CarReqDTO;
import pw.react.cars_api.data_transfer_objects.CarRespDTO;
import pw.react.cars_api.models.Car;
import pw.react.cars_api.models.Image;
import pw.react.cars_api.models.Location;
import pw.react.cars_api.models.Model;
import pw.react.cars_api.repositories.CarRepository;
import pw.react.cars_api.repositories.ImageRepository;
import pw.react.cars_api.repositories.LocationRepository;
import pw.react.cars_api.repositories.ModelRepository;
import pw.react.cars_api.repositories.*;
import pw.react.cars_api.utils.Authorization;
import pw.react.cars_api.utils.Authorization;

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;

    private final ModelRepository modelRepository;

    private final LocationRepository locationRepository;

    private final RentalRepository rentalRepository;

    private final ImageRepository imageRepository;
    private final Authorization auth;
    public CarService(CarRepository carRepository, ModelRepository modelRepository, LocationRepository locationRepository, RentalRepository rentalRepository, ImageRepository imageRepository, Authorization auth) {
        this.carRepository = carRepository;
        this.modelRepository = modelRepository;
        this.locationRepository = locationRepository;
        this.rentalRepository = rentalRepository;
        this.imageRepository = imageRepository;
        this.auth = auth;
    }

    public Page<CarRespDTO> getAllCars(Pageable page) {
        return carRepository.findAll(page)
                .map(CarRespDTO::new);
    }

    public Page<CarRespDTO> searchCars(Optional<String> brandName, Optional<String> modelName,
                                Optional<Long> productionYear, Optional<String> fuelType,
                                Optional<Long> fuelCapacity, Optional<Long> seatCount,
                                Optional<Long> doorCount, Optional<BigDecimal> dailyRate,
                                Pageable pageable) {
        Specification<Car> spec = CarSpecifications.filterByModelAttributes(
                brandName, modelName, productionYear, fuelType, fuelCapacity, seatCount, doorCount, dailyRate);
        return carRepository.findAll(spec, pageable)
                .map(CarRespDTO::new);
    }

    public Optional<CarRespDTO> getCarById(String id) {
        return carRepository.findById(id).map(CarRespDTO::new);
    }

    @Transactional
    public CarRespDTO createCar(CarReqDTO dto, String authToken) {
        auth.requireAdmin(authToken);
        Model model = modelRepository.findById(dto.modelId())
                .orElseThrow(() -> new RuntimeException("Model not found"));
        Location location = locationRepository.findById(dto.locationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));
        Image image = (dto.imageId() != null) ? imageRepository.findById(dto.imageId()).orElse(null) : null;

        Car car = new Car();
        car.setModel(model);
        car.setLocation(location);
        car.setImage(image);

        return new CarRespDTO(carRepository.save(car));
    }

    @Transactional
    public Optional<CarRespDTO> updateCar(String id, CarReqDTO dto, String authToken) {
        auth.requireAdmin(authToken);
        return carRepository.findById(id).map(car -> {
            Model model = modelRepository.findById(dto.modelId())
                    .orElseThrow(() -> new RuntimeException("Model not found"));
            Location location = locationRepository.findById(dto.locationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
            Image image = (dto.imageId() != null) ? imageRepository.findById(dto.imageId()).orElse(null) : null;

            car.setModel(model);
            car.setLocation(location);
            car.setImage(image);

            return new CarRespDTO(carRepository.save(car));
        });
    }

    @Transactional
    public boolean deleteCar(String id, String authToken) {
        auth.requireAdmin(authToken);
        if (!carRepository.existsById(id)) {
            throw new RuntimeException("Car not found");
        }
        if (rentalRepository.existsByCar_Id(id)) {
            return false;
        }
        carRepository.deleteById(id);
        return true;
    }
}
