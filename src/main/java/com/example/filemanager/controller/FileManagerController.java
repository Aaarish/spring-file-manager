package com.example.filemanager.controller;

import com.example.filemanager.dto.responses.FileDTO;
import com.example.filemanager.dto.responses.FileDeleteResponse;
import com.example.filemanager.dto.responses.FileDownloadResponse;
import com.example.filemanager.service.FileManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filemgr/files")
@RequiredArgsConstructor
public class FileManagerController {
    private final FileManagerService fileManagerService;

    @GetMapping("/{fileId}")
    public ResponseEntity<FileDownloadResponse> downloadFile(@PathVariable String fileId) {
        return ResponseEntity.ok(fileManagerService.downloadFile(fileId));
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<FileDeleteResponse> deleteFile(@PathVariable String fileId) {
        return ResponseEntity.ok(fileManagerService.deleteFile(fileId));
    }

    @GetMapping
    public ResponseEntity<List<String>> listAllFiles() {
        return ResponseEntity.ok(fileManagerService.listAllFiles());
    }
}
