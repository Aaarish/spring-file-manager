package com.example.filemanager.controller;

import com.example.filemanager.dto.FileDownloadResponse;
import com.example.filemanager.dto.FileUploadResponse;
import com.example.filemanager.service.FileUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/urls")
@RequiredArgsConstructor
public class FileUrlController {
    private final FileUrlService fileUrlService;

    @GetMapping("/upload")
    public ResponseEntity<FileUploadResponse> getFileUploadUrl(@RequestParam("filetype") String extension){
        return ResponseEntity.ok(fileUrlService.getFileUploadUrl(extension));
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<String> getFileDownloadUrl(@PathVariable("filename") String filename){
        return ResponseEntity.ok(fileUrlService.getFileDownloadUrl(filename));
    }
}
