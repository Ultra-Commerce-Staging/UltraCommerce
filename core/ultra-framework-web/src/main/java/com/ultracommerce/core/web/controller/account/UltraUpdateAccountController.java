/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.controller.account;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.web.controller.UltraAbstractController;
import com.ultracommerce.core.web.controller.account.validator.UpdateAccountValidator;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.service.CustomerService;
import com.ultracommerce.profile.web.core.CustomerState;
import com.ultracommerce.profile.web.core.service.login.LoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class UltraUpdateAccountController extends UltraAbstractController {

    @Value("${use.email.for.site.login:true}")
    protected boolean useEmailForLogin;
    
    @Resource(name="ucUserDetailsService")
    private UserDetailsService userDetailsService;

    @Resource(name = "ucCustomerService")
    protected CustomerService customerService;
    
    @Resource(name = "ucUpdateAccountValidator")
    protected UpdateAccountValidator updateAccountValidator;

    protected String accountUpdatedMessage = "Account successfully updated";
    
    protected static String updateAccountView = "account/updateAccount";
    protected static String accountRedirectView = "redirect:/account";

    public String viewUpdateAccount(HttpServletRequest request, Model model, UpdateAccountForm form) {
        Customer customer = CustomerState.getCustomer();
        form.setEmailAddress(customer.getEmailAddress());
        form.setFirstName(customer.getFirstName());
        form.setLastName(customer.getLastName());
        return getUpdateAccountView();
    }

    public String processUpdateAccount(HttpServletRequest request, Model model, UpdateAccountForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        updateAccountValidator.validate(form, result);
        
        if (result.hasErrors()) {
            return getUpdateAccountView();
        }
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
    		throw new AuthenticationCredentialsNotFoundException("Authentication was null, not authenticated, or not logged in.");
    	}
        
        Customer customer = CustomerState.getCustomer();
        customer.setEmailAddress(form.getEmailAddress());
        customer.setFirstName(form.getFirstName());
        customer.setLastName(form.getLastName());
        
        if (useEmailForLogin) {
            customer.setUsername(form.getEmailAddress());
        }
        
        customer = customerService.saveCustomer(customer);
        
        if (useEmailForLogin) {
        	UserDetails principal = userDetailsService.loadUserByUsername(customer.getUsername());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), auth.getAuthorities());
            
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        
        redirectAttributes.addFlashAttribute("successMessage", getAccountUpdatedMessage());
        return getAccountRedirectView();
    }

    public String getUpdateAccountView() {
        return updateAccountView;
    }
    
    public String getAccountRedirectView() {
        return accountRedirectView;
    }
    
    public String getAccountUpdatedMessage() {
        return accountUpdatedMessage;
    }

}
