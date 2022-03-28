package com.kutlu.teammeyt.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kutlu.teammeyt.model.Key;
import com.kutlu.teammeyt.payload.AppleIdTokenResponse;
import com.kutlu.teammeyt.payload.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Component
public class AppleRestTemplate {
    private final GenerateAppleKey generateAppleKey;

    @Value("${apple.token-uri}")
    private String tokenUrl;

    @Value("${apple.jwkSetUri}")
    private String jwkSetUrl;

    @Autowired
    public AppleRestTemplate(GenerateAppleKey generateAppleKey) {
        this.generateAppleKey = generateAppleKey;
    }

    public String getAuthTokenFromApple(TokenResponse response) throws JsonProcessingException, InvalidKeySpecException, NoSuchAlgorithmException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("client_id", response.getClientId());
        parameters.add("client_secret", response.getClientSecret());
        parameters.add("code", response.getCode());
        parameters.add("grant_type", response.getGrantType());
        parameters.add("redirect_uri", response.getRedirectUri());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(tokenUrl, entity, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            AppleIdTokenResponse appleIdTokenResponse = new ObjectMapper().readValue(responseEntity.getBody(), AppleIdTokenResponse.class);
            System.out.println(appleIdTokenResponse.getId_token());
            return getUserInfo(appleIdTokenResponse);
        }
        return "Failed to Auth Token";
    }

    private String getUserInfo(AppleIdTokenResponse appleIdTokenResponse) throws InvalidKeySpecException, NoSuchAlgorithmException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Key> responseEntity = restTemplate.getForEntity(jwkSetUrl, Key.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println(responseEntity.getBody().getKeys().size());
            return generateAppleKey.createPublicKeyApple(appleIdTokenResponse, responseEntity.getBody().getKeys());
        }

        return "Email id Not Found";
    }
}