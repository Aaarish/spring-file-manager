package com.example.filemanager.entity;

import com.example.filemanager.dto.requests.InUseWith;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileEntity {
    @Id
    private String fileId;
    private String fileName;
    private long fileSize;
    private String fileType;
    private String source;
    private String extension;
    private String visibility;
    private int duration;
    private InUseWith inUseWith;

    @JsonProperty(value = "creation_date")
    private LocalDateTime creationDate;

    @JsonProperty(value = "created_by")
    private String createdBy;

}
