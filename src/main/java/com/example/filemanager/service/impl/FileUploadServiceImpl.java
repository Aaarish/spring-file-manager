package com.example.filemanager.service.impl;

import com.example.filemanager.dao.FileEntityRepo;
import com.example.filemanager.dto.requests.InUseWith;
import com.example.filemanager.dto.responses.FileUploadResponse;
import com.example.filemanager.entity.FileEntity;
import com.example.filemanager.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    private final FileEntityRepo fileEntityRepo;
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    @Value("${amazon.s3.bucket.name}")
    private String bucketName;

    @Override
    public FileUploadResponse uploadUrl(MultipartFile file) {

        FileEntity savedFileEntity = saveMetaDataToMongoDB(file);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(savedFileEntity.getFileId())
                .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(10))
                .build();

        URL presignedUrl = s3Presigner.presignPutObject(putObjectPresignRequest).url();

        return new FileUploadResponse(presignedUrl.toString(), savedFileEntity.getFileId());
    }

    @Override
    public String uploadFile(MultipartFile file) {
        FileEntity savedFileEntity = saveMetaDataToMongoDB(file);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(savedFileEntity.getFileId())
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "File Uploaded successfully : " + savedFileEntity.getFileId();
    }

    private LocalDateTime expirationDate() {
        return LocalDateTime.now().plusWeeks(1);
    }

    private FileEntity saveMetaDataToMongoDB(MultipartFile file) {

        String filename = file.getOriginalFilename();

        FileEntity fileEntity = FileEntity.builder()
                .fileId(UUID.randomUUID().toString())
                .fileName(filename)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .creationDate(LocalDateTime.now())
                .source("file-mgr")
                .extension(filename.substring(filename.lastIndexOf(".") + 1))
                .visibility("private")
                .inUseWith(InUseWith.builder()
                        .serviceName("file-mgr")
                        .autoExpire(expirationDate())
                        .build())
                .build();

        return fileEntityRepo.save(fileEntity);
    }
}
