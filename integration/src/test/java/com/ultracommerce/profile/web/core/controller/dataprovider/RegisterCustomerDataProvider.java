/*
 * #%L
 * UltraCommerce Integration
 * %%
 * Copyright (C) 2009 - 2016 Ultra Commerce
 * %%
 * Licensed under the Ultra Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.ultracommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Ultra in which case
 * the Ultra End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.ultracommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Ultra Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package com.ultracommerce.profile.web.core.controller.dataprovider;

import com.ultracommerce.profile.core.domain.ChallengeQuestion;
import com.ultracommerce.profile.core.domain.ChallengeQuestionImpl;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.CustomerImpl;
import com.ultracommerce.profile.web.core.form.RegisterCustomerForm;
import org.testng.annotations.DataProvider;

public class RegisterCustomerDataProvider {

    @DataProvider(name = "setupCustomerControllerData")
    public static Object[][] createCustomer() {
        Customer customer = new CustomerImpl();
        customer.setEmailAddress("testCase@test.com");
        customer.setFirstName("TestFirstName");
        customer.setLastName("TestLastName");
        customer.setUsername("TestCase");
        ChallengeQuestion question = new ChallengeQuestionImpl();
        question.setId(1L);
        customer.setChallengeQuestion(question);
        customer.setChallengeAnswer("Challenge CandidateItemOfferAnswer");
        RegisterCustomerForm registerCustomer = new RegisterCustomerForm();
        registerCustomer.setCustomer(customer);
        registerCustomer.setPassword("TestPassword");
        registerCustomer.setPasswordConfirm("TestPassword");
        return new Object[][] { new Object[] { registerCustomer } };
    }
}
