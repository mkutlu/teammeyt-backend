package com.kutlu.teammeyt.controller;

import com.kutlu.teammeyt.payload.TokenResponse;
import com.kutlu.teammeyt.security.AppleRestTemplate;
import com.kutlu.teammeyt.security.GenerateAppleKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AppleController {

    private final GenerateAppleKey generateAppleKey;
    private final AppleRestTemplate appleRestTemplate;

    @Value("${apple.authorizationUri}")
    private String appleAuthorizeUrl;

    @Value("${apple.redirectUri}")
    private String redirectUri;

    @Value("${apple.clientId}")
    private String clientId;

    @Value("${apple.authorization-grant-type}")
    private String grantType;

    @Autowired
    public AppleController(GenerateAppleKey generateAppleKey, AppleRestTemplate appleRestTemplate) {
        this.generateAppleKey = generateAppleKey;
        this.appleRestTemplate = appleRestTemplate;
    }

    @GetMapping("/apple")
    public void setAuthApple(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(appleAuthorizeUrl +
                "&response_type=code" +
                "&client_id=" + clientId +
                "&scope=openid%20name%20email" +
                "&redirect_uri=" + redirectUri);
    }

    @PostMapping("/oauth2/redirect")
    public String getAppleRedirect(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("Uri:- " + request.getRequestURI());
        System.out.println("Code:-    " + request.getParameter("code"));
        TokenResponse getTokenResponse = new TokenResponse();
        getTokenResponse.setClientId(clientId);
        getTokenResponse.setClientSecret(generateAppleKey.generateSecretKey());
        getTokenResponse.setGrantType(grantType);
        getTokenResponse.setRedirectUri(redirectUri);
        getTokenResponse.setCode(request.getParameter("code"));
        return appleRestTemplate.getAuthTokenFromApple(getTokenResponse);

    }


}
