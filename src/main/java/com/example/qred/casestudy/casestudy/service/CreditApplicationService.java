package com.example.qred.casestudy.casestudy.service;

import com.example.qred.casestudy.casestudy.dtos.CreditApplicationDTO;
import com.example.qred.casestudy.casestudy.dtos.CreditApplicationStatus;
import com.example.qred.casestudy.casestudy.models.CreditApplication;
import com.example.qred.casestudy.casestudy.repository.CreditApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CreditApplicationService {

    @Autowired
    public CreditApplicationRepository creditApplicationRepository;

    public CreditApplication saveCreditApplication(CreditApplicationDTO creditApplicationDTO, Authentication authentication){
        CreditApplication creditApplication = prepareCreditApplicationForSave(creditApplicationDTO,authentication);
        return creditApplicationRepository.save(creditApplication);
    }

    public Optional<List<CreditApplication>> getAll(Authentication authentication){
        return creditApplicationRepository.findAllByLoanApplicant(authentication.getName());
    }

    public Optional<CreditApplication> findIfCreditApplicationExists(Long id){
        return creditApplicationRepository.findById(id);
    }

    public Optional<List<CreditApplication>> findAllWithStatusPending(){
        return creditApplicationRepository.findByApplicationStatus(CreditApplicationStatus.PENDING.toString());
    }

    private CreditApplication prepareCreditApplicationForSave(CreditApplicationDTO creditApplicationDTO, Authentication authentication) {
        CreditApplication creditApplication = new CreditApplication();
        creditApplication.setAmountApplied(BigDecimal.valueOf(creditApplicationDTO.getAmountApplied()));
        creditApplication.setEmail(creditApplicationDTO.getEmail());
        creditApplication.setPhoneNumber(creditApplicationDTO.getPhoneNumber());
        creditApplication.setApplicationStatus(String.valueOf(CreditApplicationStatus.PENDING));
        creditApplication.setOrganizationNumber(creditApplicationDTO.getOrganizationNumber());
        creditApplication.setLoanApplicant(authentication.getName());
        return creditApplication;
    }


    public Optional<CreditApplication> findByLoanApplicantAndApplicationStatus(Authentication authentication){
        return creditApplicationRepository.findByLoanApplicantAndApplicationStatus(authentication.getName(),CreditApplicationStatus.PENDING.toString());
    }

}
