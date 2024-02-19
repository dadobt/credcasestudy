package com.example.qred.casestudy.casestudy.repository;

import com.example.qred.casestudy.casestudy.models.CreditApplication;
import com.example.qred.casestudy.casestudy.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer,Long> {

    Optional<Offer> findOneByCreditApplication(CreditApplication creditApplication);
}
