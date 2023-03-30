package com.example.demo.repositories;

import com.example.demo.model.KeyData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeyDataRepository extends MongoRepository<KeyData, String> {

    Optional<KeyData> findKeyDataByUsername(final String username);
}
