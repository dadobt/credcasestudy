package com.example.qred.casestudy.casestudy.init;

import com.example.qred.casestudy.casestudy.controlers.CreditApplicationController;
import com.example.qred.casestudy.casestudy.models.ApplicationUser;
import com.example.qred.casestudy.casestudy.repository.ApplicationUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/*
Since there is no requirement for creating and administrating the users , we will initialize(hardcode) 2 for our purposes
*/

@Component
public class InitializeUsers {

    public static final String AGENT = "Agent";
    public static final String USER = "User";
    Logger logger = LoggerFactory.getLogger(CreditApplicationController.class);


    private ApplicationUserRepository applicationUserRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public InitializeUsers(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;

        Long id = 1L;
        String username = "user";
        String password = "password";
        String role = USER;
        String encodedPassword = passwordEncoder.encode(password);
        applicationUserRepository.save(new ApplicationUser(id,username, encodedPassword,role));

        Long id2 =2L;
        String username2 = "agent";
        String password2 = "password";
        String role2 = AGENT;
        String encodedPassword2 = passwordEncoder.encode(password2);
        applicationUserRepository.save(new ApplicationUser(id2,username2, encodedPassword2,role2));


       }
}
