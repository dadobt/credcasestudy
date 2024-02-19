package com.example.qred.casestudy.casestudy.integrationtests;


import com.example.qred.casestudy.casestudy.dtos.AuthenticationRequest;
import com.example.qred.casestudy.casestudy.dtos.AuthenticationResponse;
import com.example.qred.casestudy.casestudy.integrationtests.configuration.IntegrationTest;
import com.example.qred.casestudy.casestudy.service.MyUserDetailsService;
import com.example.qred.casestudy.casestudy.util.JwtUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthenticateControllerIT extends IntegrationTest {

    String url;
    private static final String AUTHENTICATE_ENDPOINT = "/authenticate";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Before
    public void setUp() {
        url = getBaseUrl();
    }

    @Test
    public void givenAuthenticationNeedsToBeDone_whenAuthenticateEndpointIsCalled_thenUserIsAuthenticatedAndTokenIsGenerated() {
        AuthenticationRequest authenticateRequest = createAuthenticateRequest();
        ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.postForEntity(url + AUTHENTICATE_ENDPOINT, authenticateRequest, AuthenticationResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
}
