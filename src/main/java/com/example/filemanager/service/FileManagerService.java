package com.example.filemanager.service;
import com.example.filemanager.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileManagerService {
    FileUploadResponse uploadFile(MultipartFile file) throws IOException;
}
