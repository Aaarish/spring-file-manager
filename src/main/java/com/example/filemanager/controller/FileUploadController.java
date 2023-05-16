package com.example.filemanager.controller;

import com.example.filemanager.dto.responses.FileUploadResponse;
import com.example.filemanager.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/filemgr/upload")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadService fileUploadService;

    @PostMapping("/url")
    public ResponseEntity<List<FileUploadResponse>> uploadUrl(@RequestParam("files") List<MultipartFile> files) {
        List<FileUploadResponse> responseList = new ArrayList<>();

        for (MultipartFile file : files) {
            FileUploadResponse uploadResponse = fileUploadService.uploadUrl(file);
            responseList.add(uploadResponse);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);
    }

    @PostMapping
    public ResponseEntity<List<String>> uploadFile(@RequestParam("files") List<MultipartFile> files) {
        List<String> responseList = new ArrayList<>();

        for (MultipartFile file : files) {
            String res = fileUploadService.uploadFile(file);
            responseList.add(res);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);
    }
}
