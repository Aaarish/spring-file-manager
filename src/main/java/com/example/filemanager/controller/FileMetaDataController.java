package com.example.filemanager.controller;

import com.example.filemanager.dto.responses.FileDTO;
import com.example.filemanager.service.FileMetaDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/filemgr/files/meta-data")
@RequiredArgsConstructor
public class FileMetaDataController {

    private final FileMetaDataService metaDataService;

    @GetMapping("/{fileId}")
    public ResponseEntity<FileDTO> viewFileMetaData(@PathVariable String fileId) {
        return ResponseEntity.ok(metaDataService.viewFileMetaData(fileId));
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<String> updateFileMetaData(@PathVariable String fileId, @RequestBody FileDTO fileDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metaDataService.updateFileMetaData(fileId, fileDTO));
    }
}
