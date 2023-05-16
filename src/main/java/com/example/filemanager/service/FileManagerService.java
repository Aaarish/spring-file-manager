package com.example.filemanager.service;

import com.example.filemanager.dto.responses.FileDTO;
import com.example.filemanager.dto.responses.FileDeleteResponse;
import com.example.filemanager.dto.responses.FileDownloadResponse;

import java.util.List;

public interface FileManagerService {

    FileDownloadResponse downloadFile(String fileId);

    FileDeleteResponse deleteFile(String fileId);

    List<String> listAllFiles();
}
