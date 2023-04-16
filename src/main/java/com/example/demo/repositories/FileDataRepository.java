package com.example.demo.repositories;

import com.example.demo.model.FileData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileDataRepository extends MongoRepository<FileData, String> {
    Optional<FileData> findFileDataByFileName(final String fileName);
}
