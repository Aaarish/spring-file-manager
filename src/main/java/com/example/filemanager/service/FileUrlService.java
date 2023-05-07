package com.example.filemanager.service;

import com.example.filemanager.dto.FileUploadResponse;

public interface FileUrlService {
    FileUploadResponse getFileUploadUrl(String filename);
    String getFileDownloadUrl(String filename);
}
