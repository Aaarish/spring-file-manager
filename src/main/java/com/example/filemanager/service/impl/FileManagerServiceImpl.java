package com.example.filemanager.service.impl;

import com.example.filemanager.dao.FileEntityRepo;
import com.example.filemanager.dto.responses.FileDTO;
import com.example.filemanager.dto.responses.FileDeleteResponse;
import com.example.filemanager.dto.responses.FileDownloadResponse;
import com.example.filemanager.service.FileManagerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileManagerServiceImpl implements FileManagerService {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final FileEntityRepo fileEntityRepo;
    private final ModelMapper modelMapper;

    @Value("${amazon.s3.bucket.name}")
    private String bucketName;

    @Override
    public FileDownloadResponse downloadFile(String fileId) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileId)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(1))
                .build();

        URL presignedDownloadUrl = s3Presigner.presignGetObject(getObjectPresignRequest).url();

        return FileDownloadResponse.builder()
                .downloadUrl(presignedDownloadUrl.toString())
                .fileId(fileId)
                .build();
    }

    @Override
    @Transactional
    public FileDeleteResponse deleteFile(String fileId) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileId)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        fileEntityRepo.deleteById(fileId);

        return FileDeleteResponse.builder()
                .message("File deleted successfully")
                .build();
    }

    @Override
    public List<String> listAllFiles() {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        List<String> fileList = new ArrayList<>();

        ListObjectsV2Response listObjectsResponse;
        do {
            listObjectsResponse = s3Client.listObjectsV2(listObjectsV2Request);

            List<S3Object> contents = listObjectsResponse.contents();
            for (S3Object s3Object : contents){
                fileList.add(s3Object.key());
            }

            listObjectsV2Request = listObjectsV2Request.toBuilder()
                    .continuationToken(listObjectsResponse.nextContinuationToken())
                    .build();
        }
        while (listObjectsResponse.isTruncated());

        return fileList;
    }

}
