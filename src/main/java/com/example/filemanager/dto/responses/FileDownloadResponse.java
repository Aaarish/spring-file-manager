package com.example.filemanager.dto.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDownloadResponse {

    private String downloadUrl;
    private String fileId;
}
