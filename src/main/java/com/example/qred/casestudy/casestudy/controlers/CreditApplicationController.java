package com.example.qred.casestudy.casestudy.controlers;

import com.example.qred.casestudy.casestudy.dtos.CreditApplicationDTO;
import com.example.qred.casestudy.casestudy.models.Contract;
import com.example.qred.casestudy.casestudy.models.CreditApplication;
import com.example.qred.casestudy.casestudy.models.Offer;
import com.example.qred.casestudy.casestudy.service.ContractService;
import com.example.qred.casestudy.casestudy.service.CreditApplicationService;
import com.example.qred.casestudy.casestudy.service.OfferService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
public class CreditApplicationController {

    Logger logger = LoggerFactory.getLogger(CreditApplicationController.class);
    @Autowired
    private CreditApplicationService creditApplicationService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private ContractService contractService;

    @RequestMapping(path = "/applications/apply", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Validated
    public ResponseEntity createCreditApplication(@Validated @RequestBody CreditApplicationDTO application,Authentication authentication) {
        logger.info("New application requested");
        Optional<CreditApplication> existingApplication = creditApplicationService.findByLoanApplicantAndApplicationStatus(authentication);
        if(existingApplication.isPresent()){
            logger.info("New application declined");
            return new ResponseEntity(existingApplication,HttpStatus.NOT_ACCEPTABLE);
        }
        CreditApplication creditApplication = creditApplicationService.saveCreditApplication(application,authentication);
        logger.info("New application is created");
        return new ResponseEntity(creditApplication,HttpStatus.CREATED);
    }

    @RequestMapping(path = "/applications/getOffers", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCreditApplication(Authentication authentication) {
        logger.info("New application requested");
        Optional<Offer> anOfferFromUser = offerService.findAnOfferFromUser(authentication.getName());
        if(anOfferFromUser.isEmpty()){
            return new ResponseEntity("no offer found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(anOfferFromUser,HttpStatus.OK);
    }


    @RequestMapping(path = "/applications/getContracts", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getContractsForUser(Authentication authentication) {
        logger.info("New application requested");
        Optional<List<Contract>> contractsByName = contractService.findContractsByName(authentication.getName());
        if(contractsByName.isEmpty()){
            return new ResponseEntity("No Contract Found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(contractsByName,HttpStatus.OK);
    }

    @RequestMapping(path = "/applications/offer/sign/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Validated
    public ResponseEntity signOffer(@PathVariable @NotNull Long id, Authentication authentication) {
        Contract contract = offerService.signOffer(id);
        if(contract.getId()== null){
            logger.info("Offer with id {} is not found ",id);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(contract,HttpStatus.OK);
    }

    @GetMapping(value = "/applications", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getLatestApplication(Authentication authentication) {
        Optional<List<CreditApplication>> all = creditApplicationService.getAll(authentication);
        if(all.isPresent()){
            return new ResponseEntity(all, HttpStatus.OK);
        } else
            return new ResponseEntity("", HttpStatus.OK);
    }
}
