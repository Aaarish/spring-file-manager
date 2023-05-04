package com.example.filemanager.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "files")
@Data
public class FileEntity {
    @Id
    private String fileId;
    private String fileName;
    private String fileType;
}
