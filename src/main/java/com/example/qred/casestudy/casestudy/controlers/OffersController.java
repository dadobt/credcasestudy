package com.example.qred.casestudy.casestudy.controlers;

import com.example.qred.casestudy.casestudy.models.ApplicationUser;
import com.example.qred.casestudy.casestudy.models.CreditApplication;
import com.example.qred.casestudy.casestudy.models.Offer;
import com.example.qred.casestudy.casestudy.repository.ApplicationUserRepository;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
public class OffersController {

    public static final String AGENT = "Agent";
    Logger logger = LoggerFactory.getLogger(OffersController.class);
    @Autowired
    private OfferService service;

    @Autowired
    private CreditApplicationService creditApplicationService;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @RequestMapping(path = "/offer/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Validated
    public ResponseEntity createOffer(@PathVariable @NotNull Long id, Authentication authentication) {

        ApplicationUser byUsername = applicationUserRepository.findByUsername(authentication.getName());
        if (byUsername != null  && byUsername.getRole().equalsIgnoreCase(AGENT)) {
            Optional<CreditApplication> creditApplication = creditApplicationService.findIfCreditApplicationExists(id);
            if (creditApplication.isEmpty()) {
                logger.info("Offer {} does not exists ", id);
                return new ResponseEntity("Offer does not exists", HttpStatus.NOT_FOUND);
            }
            Offer savedOffer = service.createAnOffer(creditApplication.get(), authentication.getName());
            return new ResponseEntity(savedOffer, HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/offers", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getApplications(Authentication authentication) {
        ApplicationUser byUsername = applicationUserRepository.findByUsername(authentication.getName());
        if (byUsername != null && byUsername.getRole().equalsIgnoreCase(AGENT)) {
            Optional<List<CreditApplication>> allWithStatusPending = creditApplicationService.findAllWithStatusPending();
            if (allWithStatusPending.isEmpty()) {
                logger.info("There are no more applications with status Pending");
                return new ResponseEntity("", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity(allWithStatusPending, HttpStatus.OK);

        } else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

    }
}
