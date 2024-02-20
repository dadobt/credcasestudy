package com.example.qred.casestudy.casestudy.integrationtests;

import com.example.qred.casestudy.casestudy.dtos.CreditApplicationDTO;
import com.example.qred.casestudy.casestudy.integrationtests.configuration.IntegrationTest;
import com.example.qred.casestudy.casestudy.models.CreditApplication;
import com.example.qred.casestudy.casestudy.models.Offer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OfferControllerIT extends IntegrationTest {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    String url;

    String jwt;

    @Before
    public void setUp() {
        url = getBaseUrl();
        jwt = authenticateAgent();
        deleteAllContracts();
        deleteAllOffers();
        deleteAllApplications();
    }


    @After
    public void cleanUp() {
        deleteAllContracts();
        deleteAllOffers();
        deleteAllApplications();
    }


    @Test
    public void givenAgentIsAuthenticated_whenGetOffersIsCalledAndThereIsAPendingApplication_thenTheApplicationWillBeShownAndOkWillBeReturned() {

        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<CreditApplicationDTO> requestHeaders = new HttpEntity<>(headers);
        CreditApplication dummyCreditApplication = createDummyCreditApplication();
        ResponseEntity<List<CreditApplication>> exchange = restTemplate.exchange(url + "/offers", HttpMethod.GET, requestHeaders, new ParameterizedTypeReference<List<CreditApplication>>() {
        });
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        assertEquals(exchange.getBody().get(0).getId(), dummyCreditApplication.getId());
        assertEquals(exchange.getBody().get(0).getEmail(), dummyCreditApplication.getEmail());
        assertEquals(exchange.getBody().get(0).getOrganizationNumber(), dummyCreditApplication.getOrganizationNumber());

    }


    @Test
    public void givenAgentIsAuthenticated_whenPostOfferIsCalledWithTheIdOfAPendingApplication_thenThenOfferIsCreatedOkWillBeReturned() {

        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<CreditApplicationDTO> requestHeaders = new HttpEntity<>(headers);
        CreditApplication dummyCreditApplication = createDummyCreditApplication();
        ResponseEntity<Offer> offerResponseEntity = restTemplate.postForEntity(url + "/offer/" + dummyCreditApplication.getId(), requestHeaders, Offer.class);
        assertEquals(offerResponseEntity.getStatusCode(), HttpStatus.CREATED);



    }

}