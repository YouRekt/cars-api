package pw.react.cars_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import pw.react.cars_api.models.Image;
import pw.react.cars_api.services.ImageService;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // Add image
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Image> addImage(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] imageData = file.getBytes();
        Image savedImage = imageService.saveImage(imageData);
        return new ResponseEntity<>(savedImage, HttpStatus.CREATED);
    }

    // Get image by ID
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable String id) {
        Optional<Image> image = imageService.getImageById(id);
        return image.map(value -> ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // You can change this based on the image type
                .body(value.getData())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete image by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImageById(@PathVariable String id) {
        imageService.deleteImageById(id);
        return ResponseEntity.noContent().build();
    }
}
