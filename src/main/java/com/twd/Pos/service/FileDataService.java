
package com.twd.Pos.service;

import org.springframework.web.multipart.MultipartFile;
import com.twd.Pos.dto.FoodFileResponseDTO;
import java.io.IOException;
import java.util.List;

public interface FileDataService {

    String uploadFileToFoodDirectory(MultipartFile file, Long foodId) throws IOException;

    byte[] downloadFileFromFileDirectory(String fileName) throws IOException;

    FoodFileResponseDTO downloadAllFilesByFoodId(Long foodId) throws IOException;

 
    List<FoodFileResponseDTO> getAllFoodsWithImages() throws IOException;

    String deleteFileFromFoodDirectory(String fileName) throws IOException;

    String updateFileInFoodDirectory(String oldFileName, MultipartFile newFile, Long foodId) throws IOException;
}
