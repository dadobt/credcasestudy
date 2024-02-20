package com.example.qred.casestudy.casestudy.integrationtests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuthenticateControllerIT.class,
        CreditApplicationControllerIT.class,
        OfferControllerIT.class
})
public class AppTestSuite {
}
