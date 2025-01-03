package com.twd.SpringSecurityJWT_Pos.dto.resquest;

import org.springframework.web.multipart.MultipartFile;

public class UploadImageRequest {
    private Long userId;
    private MultipartFile file;

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    } 
}
