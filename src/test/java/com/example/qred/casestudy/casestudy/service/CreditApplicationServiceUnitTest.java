package com.example.qred.casestudy.casestudy.service;

import com.example.qred.casestudy.casestudy.dtos.CreditApplicationDTO;
import com.example.qred.casestudy.casestudy.dtos.CreditApplicationStatus;
import com.example.qred.casestudy.casestudy.models.CreditApplication;
import com.example.qred.casestudy.casestudy.repository.CreditApplicationRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CreditApplicationServiceUnitTest {
    @Mock
    private Authentication authentication;
    @Mock
    private CreditApplicationRepository creditApplicationRepository;

    @InjectMocks
    private CreditApplicationService creditApplicationService;

    private CreditApplicationDTO creditApplicationDTO;
    private CreditApplication expectedCreditApplication;

    @BeforeEach
    private void setUp() {
        when(authentication.getName()).thenReturn("Bob");

        creditApplicationDTO = createCreditApplicationDTOMock();
        expectedCreditApplication = createCreditApplicationMock(creditApplicationDTO);
    }

    @Test
    void shouldSaveCreditApplication() {
        when(creditApplicationRepository.save(any(CreditApplication.class))).thenReturn(expectedCreditApplication);

        CreditApplication actualCreditApplication = creditApplicationService.saveCreditApplication(creditApplicationDTO, authentication);

        assertNotNull(actualCreditApplication);
        assertSame(expectedCreditApplication, actualCreditApplication);
    }

    @Test
    void shouldGetAllCreditApplicationsByAuthenticationName() {
        List<CreditApplication> creditApplicationList = List.of(expectedCreditApplication);

        when(creditApplicationRepository.findAllByLoanApplicant(eq(authentication.getName()))).thenReturn(Optional.of(creditApplicationList));

        Optional<List<CreditApplication>> actual = creditApplicationService.getAll(authentication);
        List<CreditApplication> actualCreditApplications = actual.get();

        assertEquals(actualCreditApplications.size(), 1);
        assertSame(expectedCreditApplication, actualCreditApplications.get(0));
    }

    @Test
    void shouldFindIfCreditApplicationExistsById() {
        when(creditApplicationRepository.findById(eq(1L))).thenReturn(Optional.of(expectedCreditApplication));

        Optional<CreditApplication> actual = creditApplicationService.findIfCreditApplicationExists(1L);
        CreditApplication actualCreditApplication = actual.get();

        assertNotNull(actualCreditApplication);
        assertSame(expectedCreditApplication, actualCreditApplication);
    }

    @Test
    void shouldFindAllWithStatusPending() {
        List<CreditApplication> expectedCreditApplications = List.of(expectedCreditApplication);

        when(creditApplicationRepository.findByApplicationStatus(eq(CreditApplicationStatus.PENDING.toString()))).thenReturn(Optional.of(expectedCreditApplications));

        Optional<List<CreditApplication>> actual = creditApplicationService.findAllWithStatusPending();
        List<CreditApplication> actualCreditApplications = actual.get();

        assertEquals(actualCreditApplications.size(), 1);
        assertEquals(actualCreditApplications.get(0).getApplicationStatus(), CreditApplicationStatus.PENDING.toString());
        assertSame(expectedCreditApplication, actualCreditApplications.get(0));
    }

    @Test
    void shouldFindByLoanApplicantAndApplicationStatusByAuthenticationName() {
        when(creditApplicationRepository.findByLoanApplicantAndApplicationStatus(eq(authentication.getName()), eq(CreditApplicationStatus.PENDING.toString())))
                .thenReturn(Optional.of(expectedCreditApplication));

        Optional<CreditApplication> actual = creditApplicationService.findByLoanApplicantAndApplicationStatus(authentication);
        CreditApplication actualCreditApplication = actual.get();

        assertNotNull(actualCreditApplication);
        assertEquals(actualCreditApplication.getApplicationStatus(), CreditApplicationStatus.PENDING.toString());
        assertEquals(expectedCreditApplication, actualCreditApplication);
    }

    @NotNull
    private static CreditApplicationDTO createCreditApplicationDTOMock() {
        CreditApplicationDTO creditApplicationDTO = new CreditApplicationDTO();
        creditApplicationDTO.setEmail("bob@email.com");
        creditApplicationDTO.setOrganizationNumber("OrganizationNumber_1");
        creditApplicationDTO.setPhoneNumber("070123123123");
        creditApplicationDTO.setAmountApplied(1L);
        return creditApplicationDTO;
    }

    @NotNull
    private CreditApplication createCreditApplicationMock(CreditApplicationDTO creditApplicationDTO) {
        CreditApplication expectedCreditApplication = new CreditApplication();
        expectedCreditApplication.setAmountApplied(BigDecimal.valueOf(creditApplicationDTO.getAmountApplied()));
        expectedCreditApplication.setEmail(creditApplicationDTO.getEmail());
        expectedCreditApplication.setPhoneNumber(creditApplicationDTO.getPhoneNumber());
        expectedCreditApplication.setApplicationStatus(String.valueOf(CreditApplicationStatus.PENDING));
        expectedCreditApplication.setOrganizationNumber(creditApplicationDTO.getOrganizationNumber());
        expectedCreditApplication.setLoanApplicant(authentication.getName());
        return expectedCreditApplication;
    }

}