package com.example.qred.casestudy.casestudy.dtos;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    private String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    private AuthenticationResponse(){}

    public String getJwt() {
        return jwt;
    }
}
