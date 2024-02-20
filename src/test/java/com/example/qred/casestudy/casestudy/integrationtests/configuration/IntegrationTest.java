package com.example.qred.casestudy.casestudy.integrationtests.configuration;

import com.example.qred.casestudy.casestudy.CasestudyApplication;
import com.example.qred.casestudy.casestudy.dtos.AuthenticationRequest;
import com.example.qred.casestudy.casestudy.dtos.AuthenticationResponse;
import com.example.qred.casestudy.casestudy.dtos.CreditApplicationStatus;
import com.example.qred.casestudy.casestudy.models.Contract;
import com.example.qred.casestudy.casestudy.models.CreditApplication;
import com.example.qred.casestudy.casestudy.models.Offer;
import com.example.qred.casestudy.casestudy.repository.ApplicationUserRepository;
import com.example.qred.casestudy.casestudy.repository.ContractRepository;
import com.example.qred.casestudy.casestudy.repository.CreditApplicationRepository;
import com.example.qred.casestudy.casestudy.repository.OfferRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.example.qred.casestudy.casestudy.service.OfferService.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = "integration")
@SpringBootTest(classes = CasestudyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class IntegrationTest {

    private static final String HTTP_LOCALHOST = "http://localhost:";

    @Value("${server.port}")
    private int port;


    protected String getBaseUrl() {
        return HTTP_LOCALHOST + port;
    }

    @Autowired
    public TestRestTemplate restTemplate;

    @Autowired
    IntegrationTestConfigurationProperties testConfigurationProperties;


    @Autowired
    public ApplicationUserRepository applicationUserRepository;

    @Autowired
    public CreditApplicationRepository creditApplicationRepository;

    @Autowired
    public ContractRepository contractRepository;

    @Autowired
    public OfferRepository offerRepository;


    public Contract createDummyContractForUser(){
        Contract contract = new Contract();
        contract.setOrganizationName("DummyOrganization");
        contract.setOrganizationType("DummyOrganizationType");
        contract.setOrganizationNumber("1111111-1111");
        contract.setAmount(BigDecimal.valueOf(Long.parseLong("12345")));
        contract.setTerm(BigInteger.valueOf(30));
        contract.setInterest(BigDecimal.valueOf(0.03));
        contract.setTotalCommission(contract.getAmount().multiply(contract.getInterest()));
        contract.setTotalAmount(contract.getAmount().add(contract.getTotalCommission()));
        contract.setDateOfSignature(Timestamp.from(Instant.now()));
        contract.setLoanApplicant("user");
        return contractRepository.save(contract);
    }

    public CreditApplication createDummyCreditApplication(){
        CreditApplication creditApplication = new CreditApplication();
        creditApplication.setAmountApplied(BigDecimal.valueOf(12345));
        creditApplication.setLoanApplicant("user");
        creditApplication.setEmail("test@test.com");
        creditApplication.setOrganizationNumber("11111111-8888");
        creditApplication.setPhoneNumber("+46701234567");
        creditApplication.setApplicationStatus(CreditApplicationStatus.PENDING.toString());
        return creditApplicationRepository.save(creditApplication);
    }
    public Offer createDummyOfferForUser(){

       CreditApplication creditApplication = new CreditApplication();
       creditApplication.setAmountApplied(BigDecimal.valueOf(12345));
       creditApplication.setLoanApplicant("user");
       creditApplication.setEmail("test@test.com");
       creditApplication.setOrganizationNumber("11111111-8888");
       creditApplication.setPhoneNumber("+46701234567");
       creditApplication.setApplicationStatus(CreditApplicationStatus.PROCESSED.toString());
       creditApplicationRepository.save(creditApplication);

        Offer offer = new Offer();
        offer.setAmount(creditApplication.getAmountApplied());
        offer.setTerm(DEFAULT_TERM_DAYS);
        offer.setCreditApplication(creditApplication);
        offer.setInterest(calculateInterestRate(creditApplication.getAmountApplied()));
        offer.setTotalCommission(calculateCommission(creditApplication.getAmountApplied(), offer.getInterest()));
        offer.setTotalAmount(calculateTotalAmount(creditApplication.getAmountApplied(), offer.getTotalCommission()));
        offer.setDayOfExpiration(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)));
        offer.setUserId("agent");

        return  offerRepository.save(offer);
    }


    public void deleteAllApplications(){
        creditApplicationRepository.deleteAll();
    }

    public void deleteAllContracts() {
        contractRepository.deleteAll();
    }

    public void deleteAllOffers() {
        offerRepository.deleteAll();
    }

    public Optional<List<CreditApplication>> findAllByLoanApplicant(String user){
        return creditApplicationRepository.findAllByLoanApplicant(user);
    }

    public Optional<List<Contract>> findAllContractsByLoanApplicant(String user){
        return contractRepository.findAllByLoanApplicant(user);
    }

    public AuthenticationRequest createAuthenticateRequest() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("user");
        authenticationRequest.setPassword("password");
        return authenticationRequest;
    }

    public AuthenticationRequest createAuthenticateAgentRequest() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("agent");
        authenticationRequest.setPassword("password");
        return authenticationRequest;
    }

    public String authenticateUser() {
        AuthenticationRequest applicationRequest = createAuthenticateRequest();
        ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.postForEntity(getBaseUrl() + "/authenticate", applicationRequest, AuthenticationResponse.class);
        AuthenticationResponse response = responseEntity.getBody();
        return response.getJwt();
    }

    public String authenticateAgent() {
        AuthenticationRequest applicationRequest = createAuthenticateAgentRequest();
        ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.postForEntity(getBaseUrl() + "/authenticate", applicationRequest, AuthenticationResponse.class);
        AuthenticationResponse response = responseEntity.getBody();
        return response.getJwt();
    }

}
