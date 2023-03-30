package com.example.demo.resources;

import com.example.demo.model.JwtRequest;
import com.example.demo.model.JwtResponse;
import com.example.demo.model.LoginData;
import com.example.demo.services.JwtService;
import com.example.demo.services.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.example.demo.config.JwtConfiguration;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
@CrossOrigin
@Slf4j
public class AuthResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtConfiguration jwtConfiguration;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private LoginService loginService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = jwtService
                .loadUserByUsername(authenticationRequest.getUsername());

        final Map<String, String> tokenMap = jwtConfiguration.generateToken(userDetails);

        return ResponseEntity.ok(JwtResponse.builder().token(tokenMap.get("token")).keyId(tokenMap.get("key")).build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> createLogin(@RequestBody final LoginData loginData){
        loginService.saveLogin(loginData);
        return ResponseEntity.ok(null);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
