package com.example.demo.repositories;

import com.example.demo.model.FileRecords;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRecordsRepository extends MongoRepository<FileRecords, String> {
}
