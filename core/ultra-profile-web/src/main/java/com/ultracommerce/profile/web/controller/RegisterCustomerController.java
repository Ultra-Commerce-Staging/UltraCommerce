/*
 * #%L
 * UltraCommerce Profile Web
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
package com.ultracommerce.profile.web.controller;

import com.ultracommerce.profile.core.domain.ChallengeQuestion;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.service.ChallengeQuestionService;
import com.ultracommerce.profile.core.service.CustomerService;
import com.ultracommerce.profile.web.controller.validator.RegisterCustomerValidator;
import com.ultracommerce.profile.web.core.form.RegisterCustomerForm;
import com.ultracommerce.profile.web.core.service.login.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller("ucRegisterCustomerController")
@RequestMapping("/registerCustomer")
/**
 * @Deprecated - Use UltraRegisterController instead
 * RegisterCustomerController is used to register a customer.
 *
 * This controller simply calls the RegistrationCustomerValidator which can be extended for custom validation and
 * then calls saveCustomer.
 */
public class RegisterCustomerController {

    // URLs For success and failure
    protected String displayRegistrationFormView = "/account/registration/registerCustomer";
    protected String registrationErrorView = displayRegistrationFormView;
    protected String registrationSuccessView = "redirect:/registerCustomer/registerCustomerSuccess.htm";

    @Resource(name="ucCustomerService")
    protected CustomerService customerService;

    @Resource(name="ucRegisterCustomerValidator")
    protected RegisterCustomerValidator registerCustomerValidator;
    
    @Resource(name="ucChallengeQuestionService")
    protected ChallengeQuestionService challengeQuestionService;

    @Resource(name="ucLoginService")
    protected LoginService loginService;

    @RequestMapping(value="registerCustomer", method = { RequestMethod.GET })
    public String registerCustomer() {
        return getDisplayRegistrationFormView();
    }

    @RequestMapping(value="registerCustomer", method = { RequestMethod.POST })
    public ModelAndView registerCustomer(@ModelAttribute("registerCustomerForm") RegisterCustomerForm registerCustomerForm,
            BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
        registerCustomerValidator.validate(registerCustomerForm, errors);
        if (! errors.hasErrors()) {
            customerService.registerCustomer(registerCustomerForm.getCustomer(), registerCustomerForm.getPassword(), registerCustomerForm.getPasswordConfirm());
            loginService.loginCustomer(registerCustomerForm.getCustomer());
            return new ModelAndView(getRegistrationSuccessView());
        } else {
            return new ModelAndView(getRegistrationErrorView());
        }
    }
    
    @RequestMapping (value="registerCustomerSuccess", method = { RequestMethod.GET })
    public String registerCustomerSuccess() {
        return "/account/registration/registerCustomerSuccess";
    }

    @ModelAttribute("registerCustomerForm")
    public RegisterCustomerForm initCustomerRegistrationForm() {
        RegisterCustomerForm customerRegistrationForm = new RegisterCustomerForm();
        Customer customer = customerService.createNewCustomer();
        customerRegistrationForm.setCustomer(customer);
        return customerRegistrationForm;
    }
    
    @ModelAttribute("challengeQuestions")
    public List<ChallengeQuestion> getChallengeQuestions() {
        return challengeQuestionService.readChallengeQuestions();
        //return null;
    }

    public String getRegistrationErrorView() {
        return registrationErrorView;
    }

    public void setRegistrationErrorView(String registrationErrorView) {
        this.registrationErrorView = registrationErrorView;
    }

    public String getRegistrationSuccessView() {
        return registrationSuccessView;
    }

    public void setRegistrationSuccessView(String registrationSuccessView) {
        this.registrationSuccessView = registrationSuccessView;
    }

    public RegisterCustomerValidator getRegisterCustomerValidator() {
        return registerCustomerValidator;
    }

    public void setRegisterCustomerValidator(RegisterCustomerValidator registerCustomerValidator) {
        this.registerCustomerValidator = registerCustomerValidator;
    }

    public String getDisplayRegistrationFormView() {
        return displayRegistrationFormView;
    }

    public void setDisplayRegistrationFormView(String displayRegistrationFormView) {
        this.displayRegistrationFormView = displayRegistrationFormView;
    }
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(ChallengeQuestion.class, new CustomChallengeQuestionEditor(challengeQuestionService));
    }

}
