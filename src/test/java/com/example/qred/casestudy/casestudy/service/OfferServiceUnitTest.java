package com.example.qred.casestudy.casestudy.service;

import com.example.qred.casestudy.casestudy.dtos.CreditApplicationDTO;
import com.example.qred.casestudy.casestudy.dtos.CreditApplicationStatus;
import com.example.qred.casestudy.casestudy.models.Contract;
import com.example.qred.casestudy.casestudy.models.CreditApplication;
import com.example.qred.casestudy.casestudy.models.Offer;
import com.example.qred.casestudy.casestudy.repository.ContractRepository;
import com.example.qred.casestudy.casestudy.repository.CreditApplicationRepository;
import com.example.qred.casestudy.casestudy.repository.OfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferServiceUnitTest {

    @Mock
    private CreditApplicationRepository creditApplicationRepository;
    @Mock
    private OfferRepository offerRepository;
    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private OfferService offerService;

    @Test
    void shouldFindAnOfferFromUserByName() {
        CreditApplication creditApplication = createCreditApplicationMock();
        Offer expectedOffer = createOfferMock(creditApplication);

        when(creditApplicationRepository.findByLoanApplicantAndApplicationStatus(eq("Bob"), eq(CreditApplicationStatus.PROCESSED.toString())))
                .thenReturn(Optional.of(creditApplication));
        when(offerRepository.findOneByCreditApplication(eq(creditApplication))).thenReturn(Optional.of(expectedOffer));

        Optional<Offer> actual = offerService.findAnOfferFromUser("Bob");
        Offer actualOffer = actual.get();

        assertNotNull(actualOffer);
        assertSame(expectedOffer, actualOffer);
    }

    @Test
    void shouldReturnEmptyIfCreditApplicationIsEmpty() {
        when(creditApplicationRepository.findByLoanApplicantAndApplicationStatus(eq("Bob"), eq(CreditApplicationStatus.PROCESSED.toString())))
                .thenReturn(Optional.empty());

        Optional<Offer> actual = offerService.findAnOfferFromUser("Bob");

        assertEquals(actual, Optional.empty());
    }

    @Test
    void shouldReturnEmptyIfOfferIsEmpty() {
        CreditApplication creditApplication = createCreditApplicationMock();

        when(creditApplicationRepository.findByLoanApplicantAndApplicationStatus(eq("Bob"), eq(CreditApplicationStatus.PROCESSED.toString())))
                .thenReturn(Optional.of(creditApplication));
        when(offerRepository.findOneByCreditApplication(eq(creditApplication))).thenReturn(Optional.empty());

        Optional<Offer> actual = offerService.findAnOfferFromUser("Bob");

        assertEquals(actual, Optional.empty());
    }

    @Test
    void shouldCreateAnOffer() {
        CreditApplication creditApplication = createCreditApplicationMock();
        creditApplication.setApplicationStatus(CreditApplicationStatus.PENDING.toString());

        Offer expectedOffer = createOfferMock(creditApplication);

        when(offerRepository.save(any())).thenReturn(expectedOffer);

        Offer actualOffer = offerService.createAnOffer(creditApplication, "Bob");

        assertNotNull(actualOffer);
        assertEquals(actualOffer.getCreditApplication().getApplicationStatus(), CreditApplicationStatus.PROCESSED.toString());
        assertSame(expectedOffer, actualOffer);
    }

    @Test
    void shouldChangeApplicationStatus() {
        CreditApplication creditApplication = createCreditApplicationMock();
        creditApplication.setApplicationStatus(CreditApplicationStatus.PENDING.toString());

        OfferService.changeApplicationStatus(creditApplication);

        assertEquals(creditApplication.getApplicationStatus(), CreditApplicationStatus.PROCESSED.toString());
    }

    @Test
    void shouldSignOffer() {
        CreditApplication creditApplication = createCreditApplicationMock();
        Offer offer = createOfferMock(creditApplication);
        Contract expectedContract = createAContractMockFromAnOfferMock(offer);

        when(offerRepository.findById(eq(1L))).thenReturn(Optional.of(offer));
        when(contractRepository.save(any())).thenReturn(expectedContract);

        offer.getCreditApplication().setApplicationStatus(CreditApplicationStatus.SIGNED.toString());
        when(offerRepository.save(eq(offer))).thenReturn(offer);

        Contract actualContract = offerService.signOffer(1L);

        assertNotNull(actualContract);
        assertEquals(offer.getCreditApplication().getApplicationStatus(), CreditApplicationStatus.SIGNED.toString());
    }

    @Test
    void shouldReturnEmptyContractIfOfferDoesNotExist() {
        when(offerRepository.findById(eq(1L))).thenReturn(Optional.empty());

        Contract actualContract = offerService.signOffer(1L);

        assertNotNull(actualContract);
        assertNull(actualContract.getId());
        assertNull(actualContract.getTerm());
        assertNull(actualContract.getAmount());
        assertNull(actualContract.getInterest());
        assertNull(actualContract.getLoanApplicant());
        assertNull(actualContract.getOrganizationNumber());
        assertNull(actualContract.getOrganizationName());
        assertNull(actualContract.getDateOfSignature());
        assertNull(actualContract.getTotalAmount());
        assertNull(actualContract.getTotalCommission());
        assertNull(actualContract.getOrganizationType());
    }

    private CreditApplication createCreditApplicationMock() {
        CreditApplication expectedCreditApplication = new CreditApplication();
        expectedCreditApplication.setAmountApplied(BigDecimal.TEN);
        expectedCreditApplication.setEmail("bob@email.com");
        expectedCreditApplication.setPhoneNumber("070123123123");
        expectedCreditApplication.setApplicationStatus(String.valueOf(CreditApplicationStatus.PROCESSED));
        expectedCreditApplication.setOrganizationNumber("Organization number 1");
        expectedCreditApplication.setLoanApplicant("Bob");
        return expectedCreditApplication;
    }

    private Offer createOfferMock(CreditApplication creditApplication) {
        Offer offer = new Offer();
        offer.setId(1L);
        offer.setAmount(creditApplication.getAmountApplied());
        offer.setTerm(BigInteger.valueOf(30));
        offer.setCreditApplication(creditApplication);
        offer.setInterest(BigDecimal.ONE);
        offer.setTotalCommission(creditApplication.getAmountApplied().multiply(offer.getInterest()));
        offer.setTotalAmount(creditApplication.getAmountApplied().add(offer.getTotalCommission()));
        offer.setDayOfExpiration(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)));
        offer.setUserId("Bob");
        return offer;
    }

    private Contract createAContractMockFromAnOfferMock(Offer offer) {
        Contract contract = new Contract();
        contract.setOrganizationName("Organization name");
        contract.setOrganizationType("Organization type");
        contract.setOrganizationNumber(offer.getCreditApplication().getOrganizationNumber());
        contract.setAmount(offer.getAmount());
        contract.setTerm(offer.getTerm());
        contract.setInterest(offer.getInterest());
        contract.setTotalAmount(offer.getTotalAmount());
        contract.setTotalCommission(offer.getTotalCommission());
        contract.setDateOfSignature(Timestamp.from(Instant.now()));
        contract.setLoanApplicant(offer.getCreditApplication().getLoanApplicant());
        return contract;
    }
}