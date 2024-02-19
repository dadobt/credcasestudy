package com.example.qred.casestudy.casestudy.service;

import com.example.qred.casestudy.casestudy.dtos.CreditApplicationStatus;
import com.example.qred.casestudy.casestudy.models.Contract;
import com.example.qred.casestudy.casestudy.models.CreditApplication;
import com.example.qred.casestudy.casestudy.models.Offer;
import com.example.qred.casestudy.casestudy.repository.ContractRepository;
import com.example.qred.casestudy.casestudy.repository.CreditApplicationRepository;
import com.example.qred.casestudy.casestudy.repository.OfferRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.example.qred.casestudy.casestudy.contsants.Constants.MAX_AMOUNT_CREDIT;

@Service
public class OfferService {

    public static BigInteger DEFAULT_TERM_DAYS = BigInteger.valueOf(30);

    @Autowired
    public CreditApplicationRepository creditApplicationRepository;
    @Autowired
    public OfferRepository offerRepository;

    @Autowired
    public ContractRepository contractRepository;


    public Optional<Offer> findAnOfferFromUser(String name) {
        Optional<CreditApplication> byLoanApplicantAndApplicationStatus = creditApplicationRepository.findByLoanApplicantAndApplicationStatus(name, CreditApplicationStatus.PROCESSED.toString());
        Optional<Offer> oneByCreditApplication;
        if(byLoanApplicantAndApplicationStatus.isEmpty()){
            return Optional.empty();
        }else{
             oneByCreditApplication = offerRepository.findOneByCreditApplication(byLoanApplicantAndApplicationStatus.get());
             if(oneByCreditApplication.isEmpty()){
                 return Optional.empty();
             }
        }
        return oneByCreditApplication;
    }
    public Offer createAnOffer(CreditApplication creditApplication, String name) {
        changeApplicationStatus(creditApplication);
        Offer offer = prepareTheOffer(creditApplication,name);
        Offer savedOffer = offerRepository.save(offer);
        return savedOffer;
    }

    public static void changeApplicationStatus(CreditApplication creditApplication) {
        //from pending can go to processed only
        if (creditApplication.getApplicationStatus().equalsIgnoreCase(CreditApplicationStatus.PENDING.toString())) {
            creditApplication.setApplicationStatus(CreditApplicationStatus.PROCESSED.toString());
        }
    }

    @NotNull
    private Offer prepareTheOffer(CreditApplication creditApplication, String name) {
        Offer offer = new Offer();
        offer.setAmount(creditApplication.getAmountApplied());
        offer.setTerm(DEFAULT_TERM_DAYS);
        offer.setCreditApplication(creditApplication);
        offer.setInterest(calculateInterestRate(creditApplication.getAmountApplied()));
        offer.setTotalCommission(calculateCommission(creditApplication.getAmountApplied(), offer.getInterest()));
        offer.setTotalAmount(calculateTotalAmount(creditApplication.getAmountApplied(), offer.getTotalCommission()));
        offer.setDayOfExpiration(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)));
        offer.setUserId(name);
        return offer;
    }

    public static BigDecimal calculateInterestRate(BigDecimal amountApplied) {
        BigDecimal subtract = amountApplied.subtract(BigDecimal.valueOf(MAX_AMOUNT_CREDIT));
        BigDecimal absoluteValueFormSubtract = subtract.abs();
        BigDecimal divide = absoluteValueFormSubtract.divide(BigDecimal.valueOf(MAX_AMOUNT_CREDIT), 2, RoundingMode.CEILING);
        BigDecimal subtract1 = BigDecimal.ONE.subtract(divide);
        BigDecimal multiply = subtract1.multiply(BigDecimal.valueOf(0.06));
        BigDecimal max = multiply.max(BigDecimal.valueOf(0.03));
        return max;
    }

    public static BigDecimal calculateTotalAmount(BigDecimal amountApplied, BigDecimal commission) {
        BigDecimal totalAmount = amountApplied.add(commission);
        return totalAmount;
    }

    public static BigDecimal calculateCommission(BigDecimal amountApplied, BigDecimal interest) {
        BigDecimal commission = amountApplied.multiply(interest);
        return commission;
    }


    @Transactional
    public Contract signOffer(Long id) {
        Optional<Offer> offer = offerRepository.findById(id);
        Contract contract = new Contract();
        if (offer.isEmpty()) {
            return contract;
        } else {
            Contract anConttractFromAnOffer = createAnConttractFromAnOffer(offer.get());
            contract = contractRepository.save(anConttractFromAnOffer);
            offer.get().getCreditApplication().setApplicationStatus(CreditApplicationStatus.SIGNED.toString());
            //save status on the offer
            offerRepository.save(offer.get());
        }

        return contract;
    }

    private Contract createAnConttractFromAnOffer(Offer offer) {
        Contract contract = new Contract();
        //TODO:pull from some API ?
        contract.setOrganizationName("");
        contract.setOrganizationType("");
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
