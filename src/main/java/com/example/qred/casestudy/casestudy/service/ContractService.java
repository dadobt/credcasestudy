package com.example.qred.casestudy.casestudy.service;

import com.example.qred.casestudy.casestudy.models.Contract;
import com.example.qred.casestudy.casestudy.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    public static BigInteger DEFAULT_TERM_DAYS = BigInteger.valueOf(30);

    @Autowired
    public ContractRepository contractRepository;

    public Optional<List<Contract>> findContractsByName(String name){
        Optional<List<Contract>> allByLoanApplicant = contractRepository.findAllByLoanApplicant(name);
        return allByLoanApplicant;
    }
}
