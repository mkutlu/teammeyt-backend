package com.kutlu.teammeyt.payload;

import lombok.Data;

@Data
public class AppleIdTokenResponse {

    private String access_token;
    private String token_type;
    private String expires_in;
    private String refresh_token;
    private String id_token;

    @Override
    public String toString() {
        return "AppleIdTokenResponse{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", id_token='" + id_token + '\'' +
                '}';
    }
}