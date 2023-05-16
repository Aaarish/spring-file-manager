package com.example.filemanager.service.impl;

import com.example.filemanager.dao.FileEntityRepo;
import com.example.filemanager.dto.responses.FileDTO;
import com.example.filemanager.entity.FileEntity;
import com.example.filemanager.service.FileMetaDataService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FileMetaDataServiceImpl implements FileMetaDataService {
    private final FileEntityRepo fileEntityRepo;
    private final ModelMapper modelMapper;

    @Override
    public FileDTO viewFileMetaData(String fileId) {
        FileEntity fileEntity = fileEntityRepo.findById(fileId)
                .orElseThrow(() -> new NoSuchElementException("No file with given file_id exists"));

        return modelMapper.map(fileEntity, FileDTO.class);
    }

    @Override
    @Transactional
    public String updateFileMetaData(String fileId, FileDTO newFileMetaData) {
        FileEntity fileEntity = fileEntityRepo.findById(fileId)
                .orElseThrow(() -> new NoSuchElementException("No file with given file_id exists"));

        fileEntity.setFileName(newFileMetaData.getFileName());
        fileEntity.getInUseWith().setServiceName(newFileMetaData.getInUseWith().getServiceName());
        fileEntity.getInUseWith().setAutoExpire(newFileMetaData.getInUseWith().getAutoExpire());

        return fileId;
    }
}
