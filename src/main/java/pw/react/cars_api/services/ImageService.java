package pw.react.cars_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pw.react.cars_api.models.Image;
import pw.react.cars_api.repositories.ImageRepository;

import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    // Save image from byte array
    @Transactional
    public Image saveImage(byte[] imageData) {
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
    public void deleteImageById(String id) {
        imageRepository.deleteById(id);
    }
}
