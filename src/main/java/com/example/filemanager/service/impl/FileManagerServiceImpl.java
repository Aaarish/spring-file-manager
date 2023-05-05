package com.example.filemanager.service.impl;

import com.example.filemanager.dao.FileEntityRepo;
import com.example.filemanager.dto.DeleteFileResponse;
import com.example.filemanager.dto.FileDownloadResponse;
import com.example.filemanager.dto.FileUploadResponse;
import com.example.filemanager.entity.FileEntity;
import com.example.filemanager.service.FileManagerService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.utils.IoUtils;

import java.io.IOException;
import java.io.InputStream;
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

        //creating request object required to store a file to s3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        //making request to store a file to s3
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        //storing file metadata to mongodb local storage
        FileEntity fileEntity = new FileEntity();

        fileEntity.setFileId(UUID.randomUUID().toString());
        fileEntity.setFileName(fileName);
        fileEntity.setFileType(file.getContentType());

        FileEntity savedFileEntity = fileEntityRepo.save(fileEntity);

        //return response
        return FileUploadResponse.builder()
                .message(savedFileEntity.getFileName()  + " is uploaded successfully")
                .build();
    }

    @Override
    public FileDownloadResponse getFileUrl(String filename) {
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        String url = s3Client.utilities().getUrl(getUrlRequest).toString();

        return FileDownloadResponse.builder()
                .downloadUrl(url)
                .build();
    }

    @Override
    public byte[] downloadFile(String filename) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        ResponseBytes<GetObjectResponse> objectAsBytes = s3Client.getObjectAsBytes(getObjectRequest);

        return objectAsBytes.asByteArray();
    }

    @Override
    public DeleteFileResponse deleteFile(String filename) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        s3Client.deleteObject(deleteObjectRequest);

        FileEntity deletedFileEntity = fileEntityRepo.deleteByFileName(filename);

        return DeleteFileResponse.builder()
                .message(deletedFileEntity.getFileName() + " is deleted successfully")
                .build();
    }
}
