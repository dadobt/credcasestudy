package com.example.qred.casestudy.casestudy.service;

import com.example.qred.casestudy.casestudy.models.Contract;
import com.example.qred.casestudy.casestudy.repository.ContractRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private ContractService contractService;

    @Test
    void shouldReturnContractsWhenSearchingByName() {
        Contract expectedContract_1 = createContractMock(1L);
        Contract expectedContract_2 = createContractMock(2L);
        List<Contract> expectedContracts = List.of(expectedContract_1, expectedContract_2);

        when(contractRepository.findAllByLoanApplicant(eq("Bob"))).thenReturn(Optional.of(expectedContracts));

        Optional<List<Contract>> actual = contractService.findContractsByName("Bob");
        List<Contract> actualContracts = actual.get();

        assertEquals(actualContracts.size(), 2);
        assertSame(expectedContracts, actualContracts);
    }

    private Contract createContractMock(Long id) {
        Contract contract = new Contract();
        contract.setId(id);
        contract.setOrganizationName("Organization name");
        contract.setOrganizationNumber("Organization number 1");
        contract.setOrganizationType("Organization type");
        contract.setAmount(BigDecimal.ONE);
        contract.setTerm(BigInteger.ONE);
        contract.setInterest(BigDecimal.ONE);
        contract.setTotalCommission(BigDecimal.TEN);
        contract.setTotalAmount(BigDecimal.TEN);
        contract.setDateOfSignature(Timestamp.from(Instant.now()));
        contract.setLoanApplicant("Bob");
        return contract;
    }
}