package com.example.filemanager.service.impl;

import com.example.filemanager.dao.FileEntityRepo;
import com.example.filemanager.dto.FileUploadResponse;
import com.example.filemanager.entity.FileEntity;
import com.example.filemanager.service.FileUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUrlServiceImpl implements FileUrlService {
    private final S3Presigner presigner;
    private final FileEntityRepo fileEntityRepo;

    @Value("${amazon.s3.bucket.name}")
    private String bucketName;

    @Override
    public FileUploadResponse getFileUploadUrl(String extension) {
        String filename = UUID.randomUUID().toString();
        String fullFileName = filename + "." + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fullFileName)
                .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(10))
                .build();

        PresignedPutObjectRequest uploadUrl = presigner.presignPutObject(putObjectPresignRequest);

        FileEntity fileEntity = FileEntity.builder()
                .fileId(UUID.randomUUID().toString())
                .fileName(filename)
                .fileType(extension)
                .build();

        fileEntityRepo.save(fileEntity);

        return FileUploadResponse.builder()
                .message(uploadUrl.url().toString() + " is successfully uploaded!")
                .build();
    }

    @Override
    public String getFileDownloadUrl(String filename) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(10))
                .build();

        PresignedGetObjectRequest downloadUrl = presigner.presignGetObject(getObjectPresignRequest);

        return downloadUrl.url().toString();
    }
}
