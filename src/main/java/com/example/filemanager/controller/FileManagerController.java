package com.example.filemanager.controller;

import com.example.filemanager.dto.FileUploadResponse;
import com.example.filemanager.service.FileManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileManagerController {
    private final FileManagerService fileManagerService;

    @PostMapping("/upload")
    public ResponseEntity<List<FileUploadResponse>> uplaodFile(@RequestParam("files") List<MultipartFile> files) throws IOException {
        List<FileUploadResponse> fileUploadResponseList = new ArrayList<>();

        for (MultipartFile file : files) {
            FileUploadResponse fileUploadResponse = fileManagerService.uploadFile(file);
            fileUploadResponseList.add(fileUploadResponse);
        }

        return ResponseEntity.ok(fileUploadResponseList);
    }
}
