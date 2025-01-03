package com.twd.SpringSecurityJWT_Pos.service;

import org.springframework.web.multipart.MultipartFile;

import com.twd.SpringSecurityJWT_Pos.dto.respones.UserDTO;

// import com.twd.SpringSecurityJWT.dto.FixedAssetFileResponseDTO;

import java.io.IOException;
import java.util.List;

public interface FileDataService {

    String uploadFileToUserDirectory(MultipartFile file, Long userId) throws IOException;

    byte[] downloadFileFromFileDirectory(String fileName) throws IOException;

    UserDTO downloadAllFilesByUserId(Long userId) throws IOException;

    List<UserDTO> getAllUserWithImages() throws IOException;
    
}
