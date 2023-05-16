package com.example.filemanager.dto.responses;

import com.example.filemanager.dto.requests.InUseWith;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {

    private String fileId;
    private String fileName;
    private long fileSize;
    private String fileType;
    private String source;
    private String extension;
    private String visibility;
    private int duration;
    private InUseWith inUseWith;
    private LocalDateTime creationDate;
    private String createdBy;

}
