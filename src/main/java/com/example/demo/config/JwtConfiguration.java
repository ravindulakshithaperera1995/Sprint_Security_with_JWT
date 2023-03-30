package com.example.demo.config;

import com.example.demo.model.KeyData;
import com.example.demo.repositories.KeyDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Component
@Slf4j
public class JwtConfiguration implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private KeyDataRepository keyDataRepository;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token, String keyId) {
        return getClaimFromToken(token, keyId, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token, String keyId) {
        return getClaimFromToken(token, keyId, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, String keyId, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token, keyId);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token, String keyId) {
        Optional<KeyData> keyData = keyDataRepository.findById(keyId);
        if(keyData.isPresent()){
            byte[] decodedData = Base64.getDecoder().decode(keyData.get().getEncodedKey());
            SecretKey originalKey = new SecretKeySpec(decodedData, keyData.get().getAlgorithm());

            return Jwts.parser().setSigningKey(originalKey).parseClaimsJws(token).getBody();
        } else {
            log.error("Key not found {}" , keyId);
        }

        return null;
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token, String keyId) {
        final Date expiration = getExpirationDateFromToken(token, keyId);
        return expiration.before(new Date());
    }

    public  Map<String, String> generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private Map<String, String> doGenerateToken(Map<String, Object> claims, String subject) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("HmacSHA512");
            SecretKey key = generator.generateKey();
            Optional<KeyData> optionalKeyData = keyDataRepository.findKeyDataByUsername(subject);
            if(optionalKeyData.isPresent()){
                keyDataRepository.delete(optionalKeyData.get());
            }

            KeyData keyData = KeyData.builder()
                    .id(UUID.randomUUID().toString())
                    .username(subject)
                    .encodedKey(Base64.getEncoder().encodeToString(key.getEncoded()))
                    .algorithm(key.getAlgorithm())
                    .build();
            keyDataRepository.save(keyData);

            String token = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                    .signWith(key, SignatureAlgorithm.HS512).compact();

            Map<String, String> tokenDataMap = new HashMap<>();
            tokenDataMap.put("key", keyData.getId());
            tokenDataMap.put("token", token);

            return tokenDataMap;
        } catch (NoSuchAlgorithmException e) {
            log.error("Encryption failed {}" , e.getMessage());
        }

        return Collections.emptyMap();
    }

    //validate token
    public Boolean validateToken(String token, String keyId, UserDetails userDetails) {
        final String username = getUsernameFromToken(token, keyId);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, keyId));
    }
}
