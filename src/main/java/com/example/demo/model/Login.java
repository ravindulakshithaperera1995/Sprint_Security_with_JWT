package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@EqualsAndHashCode
@AllArgsConstructor
@Data
@Document(collection = "logins")
public class Login {

    @Id private String id;

    @Indexed(unique = true)
    private String username;

    private String passwordHash;

    private Role role;
}
