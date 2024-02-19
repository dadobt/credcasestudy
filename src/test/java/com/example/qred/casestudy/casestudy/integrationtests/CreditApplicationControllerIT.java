package com.example.qred.casestudy.casestudy.integrationtests;

import com.example.qred.casestudy.casestudy.dtos.CreditApplicationDTO;
import com.example.qred.casestudy.casestudy.integrationtests.configuration.IntegrationTest;
import com.example.qred.casestudy.casestudy.models.Contract;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreditApplicationControllerIT extends IntegrationTest {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final String APPLICATIONS_ENDPOINT = "/applications";

    String url;

    String jwt;
    @Before
    public void setUp() {
        url = getBaseUrl();
        jwt = authenticateUser();
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
    public void givenUserIsAuthenticated_whenPostApplicationonsApplyIsCalled_thenTheApplicationisReturnedAndSaved() {
        CreditApplicationDTO creditApplicationDTO = creditApplicationDTORequest();
        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<CreditApplicationDTO> postRequest = new HttpEntity<>(creditApplicationDTO, headers);
        ResponseEntity<CreditApplicationDTO> responseEntity = restTemplate.postForEntity(url + APPLICATIONS_ENDPOINT + "/apply", postRequest, CreditApplicationDTO.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        Optional<List<CreditApplication>> creditApplicationListFromDB = findAllByLoanApplicant("user");
        CreditApplicationDTO body = responseEntity.getBody();
        assertNotNull(creditApplicationListFromDB);
        assertEquals(1,creditApplicationListFromDB.get().size());
        for (CreditApplication savedCreditApplicationInDB: creditApplicationListFromDB.get()){
        assertEquals(body.getOrganizationNumber(),savedCreditApplicationInDB.getOrganizationNumber());
     }
    }
    @Test
    public void givenUserIsAuthenticated_whenPostApplicationonsApplyIsCalledTwiceInARow_thenOnlyOneApplicationWillBeSavedTheSevcondRequestWillbeNotAcceptable() {
            CreditApplicationDTO creditApplicationDTO = creditApplicationDTORequest();
            StringBuilder sb = new StringBuilder();
            sb.append("Bearer ").append(jwt);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", sb.toString());
            headers.set("Content-Type", "application/json");
            HttpEntity<CreditApplicationDTO> postRequest = new HttpEntity<>(creditApplicationDTO, headers);
            ResponseEntity<CreditApplicationDTO> responseEntity = restTemplate.postForEntity(url + APPLICATIONS_ENDPOINT + "/apply", postRequest, CreditApplicationDTO.class);
            assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
            Optional<List<CreditApplication>> creditApplicationListFromDB = findAllByLoanApplicant("user");
            CreditApplicationDTO body = responseEntity.getBody();
            assertNotNull(creditApplicationListFromDB);
            assertEquals(1,creditApplicationListFromDB.get().size());
            for (CreditApplication savedCreditApplicationInDB: creditApplicationListFromDB.get()){
                assertEquals(body.getOrganizationNumber(),savedCreditApplicationInDB.getOrganizationNumber());
            }
            ResponseEntity<CreditApplicationDTO> responseEntity2 = restTemplate.postForEntity(url + APPLICATIONS_ENDPOINT + "/apply", postRequest, CreditApplicationDTO.class);
            assertEquals(responseEntity2.getStatusCode(), HttpStatus.NOT_ACCEPTABLE);
        }


    @Test
    public void givenUserIsAuthenticatedAndContractIsCreated_whenUserApplicationDoesNotHaveAContract_the404ShouldBeReturned() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<CreditApplicationDTO> request = new HttpEntity<>(headers);
        ResponseEntity<String> exchange = restTemplate.exchange(url + APPLICATIONS_ENDPOINT + "/getContracts", HttpMethod.GET, request, String.class);
        assertEquals(exchange.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenUserIsAuthenticatedAndContractIsCreated_whenUserApplicationHaveContract_theTheContractShouldBeReturnedAndStatus200ShouldBeReturned() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<CreditApplicationDTO> request = new HttpEntity<>(headers);
        Contract dummyContractForUser = createDummyContractForUser();
        ResponseEntity<List<Contract>> exchange = restTemplate.exchange(url + APPLICATIONS_ENDPOINT + "/getContracts", HttpMethod.GET, request, new ParameterizedTypeReference<List<Contract>>() {});
        assertEquals(exchange.getStatusCode(), HttpStatus.OK);
        assertEquals(exchange.getBody().get(0).getTotalAmount(),dummyContractForUser.getTotalAmount());
        assertEquals(exchange.getBody().get(0).getId(),dummyContractForUser.getId());
        assertEquals(exchange.getBody().get(0).getOrganizationNumber(),dummyContractForUser.getOrganizationNumber());
    }

    @Test
    public void givenUserIsAuthenticatedAndOfferIsNotCreated_whenOfferIsNotCreated__the404ShouldBeReturned() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<CreditApplicationDTO> request = new HttpEntity<>(headers);
        ResponseEntity<String> exchange = restTemplate.exchange(url + APPLICATIONS_ENDPOINT + "/getOffers", HttpMethod.GET, request,String.class);
        assertEquals(exchange.getStatusCode(), HttpStatus.NOT_FOUND);

    }



    @Test
    public void givenUserIsAuthenticatedAndOfferIsIsCreatedCreated_whenApplicationGetOffersIsCalled__theOfferShouldBeReturned() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<CreditApplicationDTO> request = new HttpEntity<>(headers);
        Offer dummyOfferForUser = createDummyOfferForUser();
        ResponseEntity<Offer> exchange = restTemplate.exchange(url + APPLICATIONS_ENDPOINT + "/getOffers", HttpMethod.GET, request, Offer.class);
        assertEquals(exchange.getStatusCode(), HttpStatus.OK);
        assertEquals(exchange.getBody().getId(),dummyOfferForUser.getId());
        assertEquals(exchange.getBody().getTotalAmount(),dummyOfferForUser.getTotalAmount());
        assertEquals(exchange.getBody().getTotalCommission(),dummyOfferForUser.getTotalCommission());
        assertEquals(exchange.getBody().getInterest(),dummyOfferForUser.getInterest());
    }



    public CreditApplicationDTO creditApplicationDTORequest() {
        CreditApplicationDTO creditApplicationDTO = new CreditApplicationDTO();
        creditApplicationDTO.setEmail("test@test.com");
        creditApplicationDTO.setAmountApplied(25000L);
        creditApplicationDTO.setOrganizationNumber("888888-1111");
        creditApplicationDTO.setPhoneNumber("+46701234567");
        return creditApplicationDTO;
    }
}
