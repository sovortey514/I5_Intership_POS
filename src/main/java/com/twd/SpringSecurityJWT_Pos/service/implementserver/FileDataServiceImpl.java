package com.twd.SpringSecurityJWT_Pos.service.implementserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.twd.SpringSecurityJWT_Pos.dto.respones.UserDTO;
import com.twd.SpringSecurityJWT_Pos.dto.resquest.FileDataDTO;
import com.twd.SpringSecurityJWT_Pos.entity.FileData;
import com.twd.SpringSecurityJWT_Pos.entity.User;
import com.twd.SpringSecurityJWT_Pos.repository.FileDataRepository;
import com.twd.SpringSecurityJWT_Pos.repository.UserRepo;
import com.twd.SpringSecurityJWT_Pos.service.FileDataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileDataServiceImpl implements FileDataService {

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private UserRepo userRepo;

    // private final String FILE_PATH = "D:\\Year5\\I5_intership\\POS\\pos_system\\src\\main\\java\\com\\twd\\Image";
    private final String FILE_PATH = "D:\\Year5\\I5_intership\\POS\\pos_system\\src\\Uploads\\";

    @Override
    public String uploadFileToUserDirectory(MultipartFile file, Long userId) throws IOException {
        // Check if userId is provided
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        // Retrieve User from the database
        Optional<User> userOpt = userRepo.findById(userId);
        if (!userOpt.isPresent()) {
            throw new IOException("User not found with ID: " + userId);
        }

        User user = userOpt.get();
        String filePath = FILE_PATH + file.getOriginalFilename(); // Absolute path

        // Create and save FileData with the user association
        FileData fileData = FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath)
                .user(user) // Associate the file with the user
                .build();

        // Save file data to the database
        FileData savedFileData = fileDataRepository.save(fileData);

        // Save the file to the file system
        file.transferTo(new java.io.File(filePath));

        if (savedFileData != null) {
            return "User profile image uploaded successfully: " + file.getOriginalFilename()
                    + " and Files uploaded path is: " + filePath;
        } else {
            throw new IOException("Failed to save file data to the database");
        }
    }

    @Override
    public byte[] downloadFileFromFileDirectory(String fileName) throws IOException {
        Optional<FileData> fileDataObj = fileDataRepository.findByName(fileName);

        if (fileDataObj.isPresent()) {
            String filePath = fileDataObj.get().getFilePath();
            Path path = Paths.get(filePath);

            // Check if file exists before reading
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
    public UserDTO downloadAllFilesByUserId(Long userId) throws IOException {
        // Check if userId is provided
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        // Retrieve User from the database
        Optional<User> userOpt = userRepo.findById(userId);
        if (!userOpt.isPresent()) {
            throw new IOException("User not found with ID: " + userId);
        }

        User user = userOpt.get();
        List<FileData> fileDataList = fileDataRepository.findByUser(user);

        List<FileDataDTO> fileDataDTOs = new ArrayList<>();
        for (FileData fileData : fileDataList) {
            Path path = Paths.get(fileData.getFilePath());
            if (Files.exists(path)) {
                FileDataDTO fileDataDTO = new FileDataDTO();
                fileDataDTO.setFileName(fileData.getName());
                fileDataDTO.setFileType(fileData.getType());

                // Construct file URL
                String fileUrl = "http://localhost:6060/auth/get_image/" + fileData.getName();
                fileDataDTO.setFileUrl(fileUrl);

                fileDataDTOs.add(fileDataDTO);
            } else {
                throw new IOException("File not found at path: " + fileData.getFilePath());
            }
        }

        // Create the response DTO for the user
        UserDTO responseDTO = new UserDTO();
        responseDTO.setId(user.getId());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setName(user.getName());
        responseDTO.setPassword(user.getPassword());
        responseDTO.setRole(user.getrole());
        responseDTO.setUsername(user.getUsername());
        responseDTO.setFiles(fileDataDTOs);

        return responseDTO;
    }

    @Override
    public List<UserDTO> getAllUserWithImages() throws IOException {
        List<User> users = userRepo.findAll();
        List<UserDTO> responseList = new ArrayList<>();

        for (User user : users) {
            List<FileData> fileDataList = fileDataRepository.findByUser(user);

            List<FileDataDTO> fileDataDTOs = new ArrayList<>();
            for (FileData fileData : fileDataList) {
                Path path = Paths.get(fileData.getFilePath());
                if (Files.exists(path)) {
                    FileDataDTO fileDataDTO = new FileDataDTO();
                    fileDataDTO.setFileName(fileData.getName());
                    fileDataDTO.setFileType(fileData.getType());

                    // Construct file URL
                    String fileUrl = "http://localhost:9090/auth/get_image/" + fileData.getName();
                    fileDataDTO.setFileUrl(fileUrl);

                    fileDataDTOs.add(fileDataDTO);
                } else {
                    throw new IOException("File not found at path: " + fileData.getFilePath());
                }
            }

            // Create the response DTO for the user
            UserDTO responseDTO = new UserDTO();
            responseDTO.setId(user.getId());
            responseDTO.setEmail(user.getEmail());
            responseDTO.setName(user.getName());
            responseDTO.setPassword(user.getPassword());
            responseDTO.setRole(user.getrole());
            responseDTO.setUsername(user.getUsername());
            responseDTO.setFiles(fileDataDTOs);

            responseList.add(responseDTO);
        }

        return responseList;
    }
}

