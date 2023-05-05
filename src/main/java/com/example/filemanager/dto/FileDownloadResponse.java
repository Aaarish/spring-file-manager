package com.example.filemanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDownloadResponse {
    private String downloadUrl;
    private byte[] file;
}
