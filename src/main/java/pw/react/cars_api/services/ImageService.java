package pw.react.cars_api.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pw.react.cars_api.models.Image;
import pw.react.cars_api.repositories.ImageRepository;
import pw.react.cars_api.utils.Authorization;

import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final Authorization auth;
    public ImageService(ImageRepository imageRepository, Authorization auth) {
        this.imageRepository = imageRepository;
        this.auth = auth;
    }

    // Save image from byte array
    @Transactional
    public Image saveImage(byte[] imageData, String authToken) {
        auth.requireAdmin(authToken);
        Image image = new Image();
        image.setData(imageData);
        return imageRepository.save(image);
    }

    // Get an image by ID
    public Optional<Image> getImageById(String id) {
        return imageRepository.findById(id);
    }

    // Delete image by ID
    @Transactional
    public void deleteImageById(String id, String authToken) {
        auth.requireAdmin(authToken);
        imageRepository.deleteById(id);
    }
}
