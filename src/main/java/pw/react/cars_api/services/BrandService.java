package pw.react.cars_api.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pw.react.cars_api.data_transfer_objects.BrandReqDTO;
import pw.react.cars_api.data_transfer_objects.BrandRespDTO;
import pw.react.cars_api.models.Brand;
import pw.react.cars_api.repositories.BrandRepository;
import pw.react.cars_api.utils.Authorization;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final Authorization auth;

    public BrandService(Authorization auth, BrandRepository brandRepository) {
        this.auth = auth;
        this.brandRepository = brandRepository;
    }

    public List<BrandRespDTO> getAllBrands() {
        return brandRepository.findAll().stream()
                              .map(BrandRespDTO::new)
                              .toList();
    }

    @Transactional
    public BrandRespDTO addBrand(BrandReqDTO dto, String authToken) {
        auth.requireAdmin(authToken);
        Brand brand = new Brand();
        brand.setName(dto.name());
        brand.setShortName(dto.shortName());
        return new BrandRespDTO(brandRepository.save(brand));
    }

    @Transactional
    public void deleteBrandByName(String name, String authToken) {
        auth.requireAdmin(authToken);
        Optional<Brand> brand = brandRepository.findByName(name);
        brand.ifPresent(brandRepository::delete);
    }
}
