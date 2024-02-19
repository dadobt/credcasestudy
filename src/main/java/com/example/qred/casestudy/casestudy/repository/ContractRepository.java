package com.example.qred.casestudy.casestudy.repository;

import com.example.qred.casestudy.casestudy.models.Contract;
import com.example.qred.casestudy.casestudy.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract,Long> {

    Optional<List<Contract>> findAllByLoanApplicant (String LoanApplicant);
}
