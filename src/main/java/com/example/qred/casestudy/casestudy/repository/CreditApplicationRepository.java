package com.example.qred.casestudy.casestudy.repository;

import com.example.qred.casestudy.casestudy.models.CreditApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditApplicationRepository extends JpaRepository<CreditApplication,Long> {

    Optional<CreditApplication> findByLoanApplicantAndApplicationStatus(String name, String ApplicationStatus);

    Optional<List<CreditApplication>> findAllByLoanApplicant(String name);

    Optional<List<CreditApplication>> findByApplicationStatus(String status);

}
