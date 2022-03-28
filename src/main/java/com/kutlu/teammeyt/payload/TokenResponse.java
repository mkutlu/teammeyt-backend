package com.kutlu.teammeyt.payload;

import lombok.Data;

/**
 * The type Token Response. Using as object for token data transfer to user.
 */
@Data
public class TokenResponse {
    private String clientId;
    private String clientSecret;
    private String code;
    private String grantType;
    private String redirectUri;
}