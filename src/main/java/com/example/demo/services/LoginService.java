package com.example.demo.services;

import com.example.demo.model.Login;
import com.example.demo.model.LoginData;
import com.example.demo.model.Role;
import com.example.demo.repositories.LoginRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    public void saveLogin(final LoginData loginData){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(loginData.getPassword());

        Login login = Login.builder()
                .id(UUID.randomUUID().toString())
                .username(loginData.getUsername())
                .passwordHash(encodedPassword)
                .role(Role.ADMIN)
                .build();

        loginRepository.save(login);
    }

    public Login findByUsername(final String username){
        return loginRepository.findLoginByUsername(username).orElse(null);
    }
}
