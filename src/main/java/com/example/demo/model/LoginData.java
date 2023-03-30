package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
@AllArgsConstructor
@Data
public class LoginData {

    private String username;
    private String password;
}
