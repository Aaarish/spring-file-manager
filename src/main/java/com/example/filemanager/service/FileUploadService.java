package com.example.filemanager.service;

import com.example.filemanager.dto.responses.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    FileUploadResponse uploadUrl(MultipartFile file);

    String uploadFile(MultipartFile file);
}
