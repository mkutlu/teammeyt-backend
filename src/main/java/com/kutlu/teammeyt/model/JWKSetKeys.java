package com.kutlu.teammeyt.model;

import lombok.Data;

@Data
public class JWKSetKeys {
    private String kty;
    private String kid;
    private String use;
    private String alg;
    private String n;
    private String e;
}