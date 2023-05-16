package com.example.filemanager.service;

import com.example.filemanager.dto.responses.FileDTO;

public interface FileMetaDataService {

    FileDTO viewFileMetaData(String fileId);

    String updateFileMetaData(String fileId, FileDTO newFileMetaData);
}
