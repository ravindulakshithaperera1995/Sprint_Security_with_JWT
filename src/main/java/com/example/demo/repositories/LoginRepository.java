package com.example.demo.repositories;

import com.example.demo.model.Login;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends MongoRepository<Login, String> {

    Optional<Login> findLoginByUsername(final String username);
}
