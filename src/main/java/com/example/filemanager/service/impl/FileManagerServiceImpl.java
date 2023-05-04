package com.example.filemanager.service.impl;

import com.example.filemanager.dao.FileEntityRepo;
import com.example.filemanager.dto.FileUploadResponse;
import com.example.filemanager.entity.FileEntity;
import com.example.filemanager.service.FileManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileManagerServiceImpl implements FileManagerService {
    private final FileEntityRepo fileEntityRepo;
    private final S3Client s3Client;

    @Value("${amazon.s3.bucket.name}")
    private String bucketName;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        FileEntity fileEntity = new FileEntity();

        fileEntity.setFileId(UUID.randomUUID().toString());
        fileEntity.setFileName(fileName);
        fileEntity.setFileType(file.getContentType());

        FileEntity savedFileEntity = fileEntityRepo.save(fileEntity);

        return FileUploadResponse.builder()
                .message(savedFileEntity  + " is uploaded successfully")
                .build();
    }
}
