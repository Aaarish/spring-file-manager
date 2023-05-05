package com.example.filemanager.controller;

import com.example.filemanager.dto.DeleteFileResponse;
import com.example.filemanager.dto.FileDownloadResponse;
import com.example.filemanager.dto.FileUploadResponse;
import com.example.filemanager.entity.FileEntity;
import com.example.filemanager.service.FileManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileManagerController {
    private final FileManagerService fileManagerService;

    @PostMapping("/upload")
    public ResponseEntity<List<FileUploadResponse>> uploadFile(@RequestParam("files") List<MultipartFile> files) throws IOException {
        List<FileUploadResponse> fileUploadResponseList = new ArrayList<>();

        for (MultipartFile file : files) {
            FileUploadResponse fileUploadResponse = fileManagerService.uploadFile(file);
            fileUploadResponseList.add(fileUploadResponse);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(fileUploadResponseList);
    }

    @GetMapping("/download/url/{filename}")
    public ResponseEntity<FileDownloadResponse> getFileUrl(@PathVariable("filename") String filename){
        return ResponseEntity.ok(fileManagerService.getFileUrl(filename));
    }

    @GetMapping(value = "/download/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadFile(@PathVariable("filename") String filename) {
        return ResponseEntity.ok(fileManagerService.downloadFile(filename));
    }

    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<DeleteFileResponse> deleteFile(@PathVariable("filename") String filename){
        return ResponseEntity.ok(fileManagerService.deleteFile(filename));
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileEntity>> listFiles(){
        return ResponseEntity.ok(fileManagerService.listFiles());
    }
}
