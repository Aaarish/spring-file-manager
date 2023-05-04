package com.example.filemanager.dao;

import com.example.filemanager.entity.FileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileEntityRepo extends MongoRepository<FileEntity, String> {
}
