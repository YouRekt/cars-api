package pw.react.cars_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import pw.react.cars_api.data_transfer_objects.ImageRespDTO;
import pw.react.cars_api.models.Image;
import pw.react.cars_api.services.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // Add image
    @PostMapping(name="/",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Post new image.")
    public ResponseEntity<Image> addImage(@RequestParam("file") MultipartFile file, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) throws IOException {
        byte[] imageData = file.getBytes();
        Image savedImage = imageService.saveImage(imageData, authorization);
        return new ResponseEntity<>(savedImage, HttpStatus.CREATED);
    }

    @GetMapping("/")
    @Operation(summary = "List images.")
    public ResponseEntity<List<ImageRespDTO>> listImages() {
        List<Image> images = imageService.getAllImages();
        return ResponseEntity.of(Optional.of(images.stream().map(ImageRespDTO::new).toList()));
    }

    // Get image by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get image by id.")
    public ResponseEntity<byte[]> getImageById(@PathVariable String id) {
        Optional<Image> image = imageService.getImageById(id);
        return image.map(value -> ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // You can change this based on the image type
                .body(value.getData())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete image by ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete image by id.")
    public ResponseEntity<Void> deleteImageById(@PathVariable String id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorization) {
        imageService.deleteImageById(id, authorization);
        return ResponseEntity.noContent().build();
    }
}
