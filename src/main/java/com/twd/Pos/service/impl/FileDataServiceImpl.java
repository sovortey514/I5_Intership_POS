
package com.twd.Pos.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.twd.Pos.dto.FileDataDTO;
import com.twd.Pos.dto.FoodFileResponseDTO;
import com.twd.Pos.entity.FileData;
import com.twd.Pos.entity.Food;
import com.twd.Pos.repository.FileDataRepository;
import com.twd.Pos.repository.FoodRepository;
import com.twd.Pos.service.FileDataService;

@Service
public class FileDataServiceImpl implements FileDataService {

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private FoodRepository foodRepository;

    private final String FOOD_IMAGE_PATH = "D:\\Year4\\project_Intern\\Fixed_Asset\\src\\Uploads\\";

    
    // @Override
    // public String uploadFileToFoodDirectory(MultipartFile file, Long foodId) throws IOException {
    //     if (foodId == null) {
    //         throw new IllegalArgumentException("Food ID must not be null");
    //     }

    //     Optional<Food> foodOpt = foodRepository.findById(foodId);
    //     if (!foodOpt.isPresent()) {
    //         throw new IOException("Food not found with ID: " + foodId);
    //     }

    //     Food food = foodOpt.get();
    //     String filePath = FOOD_IMAGE_PATH + file.getOriginalFilename();

    //     // Debugging log
    //     System.out.println("Saving file: " + file.getOriginalFilename() + " for Food ID: " + food.getId());

    //     FileData fileData = new FileData();
    //     fileData.setName(file.getOriginalFilename());
    //     fileData.setType(file.getContentType());
    //     fileData.setFilePath(filePath);
    //     fileData.setFood(food); // ✅ Ensure correct linking

    //     FileData savedFile = fileDataRepository.save(fileData);
    //     file.transferTo(new File(filePath));

    //     // Debugging log to check database save
    //     System.out.println("Saved file with ID: " + savedFile.getId() + ", Food ID: " + savedFile.getFood().getId());

    //     return "File uploaded successfully: " + file.getOriginalFilename();
    // }

    @Override
public String uploadFileToFoodDirectory(MultipartFile file, Long foodId) throws IOException {
    if (foodId == null) {
        throw new IllegalArgumentException("Food ID must not be null");
    }

    Optional<Food> foodOpt = foodRepository.findById(foodId);
    if (!foodOpt.isPresent()) {
        throw new IOException("Food not found with ID: " + foodId);
    }

    Food food = foodOpt.get();
    String originalFilename = file.getOriginalFilename();
    String filePath = FOOD_IMAGE_PATH + originalFilename;

    // Check if a file with the same name already exists
    int counter = 1;
    while (fileDataRepository.existsByFilePath(filePath)) {
        // Generate a new file name
        String extension = "";
        String nameWithoutExt = originalFilename;
        
        if (originalFilename.contains(".")) {
            int lastIndex = originalFilename.lastIndexOf(".");
            nameWithoutExt = originalFilename.substring(0, lastIndex);
            extension = originalFilename.substring(lastIndex);
        }

        // Append a counter to the filename
        filePath = FOOD_IMAGE_PATH + nameWithoutExt + "_" + counter + extension;
        counter++;
    }

    // Debugging log
    System.out.println("Saving file: " + filePath + " for Food ID: " + food.getId());

    FileData fileData = new FileData();
    fileData.setName(new File(filePath).getName());
    fileData.setType(file.getContentType());
    fileData.setFilePath(filePath);
    fileData.setFood(food);

    FileData savedFile = fileDataRepository.save(fileData);
    file.transferTo(new File(filePath));

    // Debugging log to check database save
    System.out.println("Saved file with ID: " + savedFile.getId() + ", Food ID: " + savedFile.getFood().getId());

    return "File uploaded successfully: " + savedFile.getName();
}


    @Override
    public byte[] downloadFileFromFileDirectory(String fileName) throws IOException {
        Optional<FileData> fileDataObj = fileDataRepository.findByName(fileName);

        if (fileDataObj.isPresent()) {
            String filePath = fileDataObj.get().getFilePath();
            Path path = Paths.get(filePath);

            if (Files.exists(path)) {
                return Files.readAllBytes(path);
            } else {
                throw new IOException("File not found");
            }
        } else {
            throw new IOException("File data not found in the database");
        }
    }

    @Override
    public FoodFileResponseDTO downloadAllFilesByFoodId(Long foodId) throws IOException {
        Optional<Food> foodOpt = foodRepository.findById(foodId);
        if (!foodOpt.isPresent()) {
            throw new IOException("Food not found with ID: " + foodId);
        }

        Food food = foodOpt.get();
        List<FileData> fileDataList = fileDataRepository.findByFoodId(food.getId()); 

        List<FileDataDTO> fileDataDTOs = fileDataList.stream()
                .map(FileDataDTO::new) 
                .collect(Collectors.toList());

        return new FoodFileResponseDTO(food, fileDataDTOs); 
    }


    @Override
    public List<FoodFileResponseDTO> getAllFoodsWithImages() {
        List<Food> foods = foodRepository.findAll();

        return foods.stream().map(food -> {
            System.out.println("Fetching files for Food ID: " + food.getId());

            List<FileDataDTO> files = fileDataRepository.findByFoodId(food.getId()) // ✅ Fetch images
                    .stream()
                    .map(FileDataDTO::new)
                    .collect(Collectors.toList());

            System.out.println("Found " + files.size() + " files for Food ID: " + food.getId());

            return new FoodFileResponseDTO(food, files);
        }).collect(Collectors.toList());
    }

    @Override
    public String deleteFileFromFoodDirectory(String fileName) throws IOException {
        Optional<FileData> fileDataOpt = fileDataRepository.findByName(fileName);
        
        if (!fileDataOpt.isPresent()) {
            throw new IOException("File not found in database: " + fileName);
        }

        FileData fileData = fileDataOpt.get();
        String filePath = fileData.getFilePath();
        File file = new File(filePath);

        
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Failed to delete file from directory: " + filePath);
            }
        }

  
        fileDataRepository.delete(fileData);

        return "File deleted successfully: " + fileName;
    }

    @Override
    public String updateFileInFoodDirectory(String oldFileName, MultipartFile newFile, Long foodId) throws IOException {

        deleteFileFromFoodDirectory(oldFileName);

        return uploadFileToFoodDirectory(newFile, foodId);
    }

}
