// // package com.twd.Pos.service;

// // import org.springframework.web.multipart.MultipartFile;

// // import com.twd.Pos.dto.FixedAssetFileResponseDTO;
// // import com.twd.Pos.dto.FoodFileResponseDTO;

// // import java.io.IOException;
// // import java.util.List;
// // public interface FileDataService {

// //     String uploadFileToFileDirectory(MultipartFile file, Long fixedAssetId) throws IOException;

// //     String uploadFileToUserDirectory(MultipartFile file, Long userId) throws IOException;

// //     byte[] downloadFileFromFileDirectory(String fileName) throws IOException;

// //     FixedAssetFileResponseDTO downloadAllFilesByFixedAssetId(Long fixedAssetId) throws IOException;

// //     List<FixedAssetFileResponseDTO> getAllAssetsWithImages() throws IOException;

// //     String uploadFileToFoodDirectory(MultipartFile file, Long foodId) throws IOException;

// //     List<FoodFileResponseDTO> getAllFoodsWithImages() throws IOException;
// // }
// package com.twd.Pos.service;

// import org.springframework.web.multipart.MultipartFile;
// import com.twd.Pos.dto.FoodFileResponseDTO;
// import java.io.IOException;
// import java.util.List;

// public interface FileDataService {

//     // ✅ Upload a file for a food item
//     String uploadFileToFoodDirectory(MultipartFile file, Long foodId) throws IOException;

//     // ✅ Upload file for a user (if needed)
//     String uploadFileToUserDirectory(MultipartFile file, Long userId) throws IOException;

//     // ✅ Download file by filename
//     byte[] downloadFileFromFileDirectory(String fileName) throws IOException;

//     // ✅ Get all files related to a specific food item
//     FoodFileResponseDTO downloadAllFilesByFoodId(Long foodId) throws IOException;

//     // ✅ Get all foods with images
//     List<FoodFileResponseDTO> getAllFoodsWithImages() throws IOException;
// }
package com.twd.Pos.service;

import org.springframework.web.multipart.MultipartFile;
import com.twd.Pos.dto.FoodFileResponseDTO;
import java.io.IOException;
import java.util.List;

public interface FileDataService {

    // ✅ Upload a file for a food item
    String uploadFileToFoodDirectory(MultipartFile file, Long foodId) throws IOException;

    // ✅ Download file by filename
    byte[] downloadFileFromFileDirectory(String fileName) throws IOException;

    // ✅ Get all files related to a specific food item
    FoodFileResponseDTO downloadAllFilesByFoodId(Long foodId) throws IOException;

    // ✅ Get all foods with images
    List<FoodFileResponseDTO> getAllFoodsWithImages() throws IOException;
}
