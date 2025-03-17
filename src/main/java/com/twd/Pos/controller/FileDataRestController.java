
package com.twd.Pos.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.twd.Pos.dto.FoodFileResponseDTO;
import com.twd.Pos.service.FileDataService;

@RestController
@RequestMapping("/admin")
public class FileDataRestController {

    @Autowired
    private FileDataService fileDataService;

    private final String UPLOAD_DIRECTORY = "src/Uploads";

    @PostMapping("/upload_food_image")
    public ResponseEntity<?> uploadImageToFoodDirectory(
            @RequestParam("file") MultipartFile file,
            @RequestParam("foodId") Long foodId) throws IOException {

   
        String uploadFile = fileDataService.uploadFileToFoodDirectory(file, foodId);

        return ResponseEntity.status(HttpStatus.OK).body(uploadFile);
    }

    @GetMapping("/get_images_by_food/{foodId}")
    public ResponseEntity<?> downloadAllImagesByFoodId(@PathVariable Long foodId) {
        try {
            FoodFileResponseDTO files = fileDataService.downloadAllFilesByFoodId(foodId);
            return ResponseEntity.status(HttpStatus.OK).body(files);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error downloading files: " + e.getMessage());
        }
    }

    @GetMapping("/get_image/{fileName}")
    public ResponseEntity<?> downloadImageFromFileDirectory(@PathVariable String fileName) {
        try {
            byte[] downloadFile = fileDataService.downloadFileFromFileDirectory(fileName);

            MediaType mediaType;
            if (fileName.endsWith(".png")) {
                mediaType = MediaType.IMAGE_PNG;
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else {
                mediaType = MediaType.APPLICATION_OCTET_STREAM; // Fallback
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(mediaType)
                    .body(downloadFile);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error downloading file: " + e.getMessage());
        }
    }


    @GetMapping("/get_all_foods_with_images")
    public ResponseEntity<?> getAllFoodsWithImages() {
        try {
            List<FoodFileResponseDTO> foodsWithImages = fileDataService.getAllFoodsWithImages();
            return ResponseEntity.status(HttpStatus.OK).body(foodsWithImages);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving foods: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete_file/{fileName}")
    public ResponseEntity<?> deleteFile(@PathVariable String fileName) {
        try {
            String response = fileDataService.deleteFileFromFoodDirectory(fileName);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting file: " + e.getMessage());
        }
    }

    @PutMapping("/update_file/{oldFileName}")
    public ResponseEntity<?> updateFile(
            @PathVariable String oldFileName,
            @RequestParam("file") MultipartFile newFile,
            @RequestParam("foodId") Long foodId) {
        try {
            String response = fileDataService.updateFileInFoodDirectory(oldFileName, newFile, foodId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating file: " + e.getMessage());
        }
    }
    
}
