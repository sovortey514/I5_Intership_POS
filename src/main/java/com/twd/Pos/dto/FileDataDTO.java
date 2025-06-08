package com.twd.Pos.dto;

import com.twd.Pos.entity.FileData;

import lombok.Data;

@Data
public class FileDataDTO {

    // private String fileName;
    // private String fileType;
    // private String fileUrl;

    // Constructors, Getters, and Setters

     private Long id;
    private String fileName;
    private String fileType;
    private String fileUrl;

    // ✅ Constructor that accepts FileData
    public FileDataDTO(FileData fileData) {
        this.id = fileData.getId();
        this.fileName = fileData.getName();
        this.fileType = fileData.getType();
        this.fileUrl = "http://localhost:6060/auth/get_image/" + fileData.getName();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
}
