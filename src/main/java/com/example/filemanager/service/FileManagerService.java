package com.example.filemanager.service;
import com.example.filemanager.dto.DeleteFileResponse;
import com.example.filemanager.dto.FileDownloadResponse;
import com.example.filemanager.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileManagerService {
    FileUploadResponse uploadFile(MultipartFile file) throws IOException;
    FileDownloadResponse getFileUrl(String filename);

    byte[] downloadFile(String filename);

    DeleteFileResponse deleteFile(String filename);
}
