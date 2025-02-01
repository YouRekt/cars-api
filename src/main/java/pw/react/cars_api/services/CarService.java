package pw.react.cars_api.services;

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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;

    private final ModelRepository modelRepository;

    private final LocationRepository locationRepository;

    private final ImageRepository imageRepository;

    public CarService(CarRepository carRepository, ModelRepository modelRepository, LocationRepository locationRepository, ImageRepository imageRepository) {
        this.carRepository = carRepository;
        this.modelRepository = modelRepository;
        this.locationRepository = locationRepository;
        this.imageRepository = imageRepository;
    }

    public List<CarRespDTO> getAllCars() {
        return carRepository.findAll().stream()
                .map(CarRespDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<CarRespDTO> getCarById(String id) {
        return carRepository.findById(id).map(CarRespDTO::new);
    }

    @Transactional
    public CarRespDTO createCar(CarReqDTO dto) {
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
    public Optional<CarRespDTO> updateCar(String id, CarReqDTO dto) {
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
    public void deleteCar(String id) {
        if (!carRepository.existsById(id)) {
            throw new RuntimeException("Car not found");
        }
        carRepository.deleteById(id);
    }
}
