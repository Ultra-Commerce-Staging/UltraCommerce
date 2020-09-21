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

import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.security.util.PasswordChange;
import com.ultracommerce.common.web.controller.UltraAbstractController;
import com.ultracommerce.core.web.controller.account.validator.ChangePasswordValidator;
import com.ultracommerce.profile.core.service.CustomerService;
import com.ultracommerce.profile.web.core.CustomerState;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * This controller handles password changes for a customer's account
 */
public class UltraChangePasswordController extends UltraAbstractController {

    @Resource(name = "ucCustomerService")
    protected CustomerService customerService;
    @Resource(name = "ucChangePasswordValidator")
    protected ChangePasswordValidator changePasswordValidator;

    protected String passwordChangedMessage = "Password successfully changed";
    
    protected static String changePasswordView = "account/changePassword";
    protected static String changePasswordRedirect = "redirect:/account/password";

    public String viewChangePassword(HttpServletRequest request, Model model) {
        return getChangePasswordView();
    }

    public String processChangePassword(HttpServletRequest request, Model model, ChangePasswordForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        PasswordChange passwordChange = new PasswordChange(CustomerState.getCustomer().getUsername());
        passwordChange.setCurrentPassword(form.getCurrentPassword());
        passwordChange.setNewPassword(form.getNewPassword());
        passwordChange.setNewPasswordConfirm(form.getNewPasswordConfirm());
        changePasswordValidator.validate(passwordChange, result);
        if (result.hasErrors()) {
            return getChangePasswordView();
        }
        customerService.changePassword(passwordChange);
        return getChangePasswordRedirect();
    }

    public String getChangePasswordView() {
        return changePasswordView;
    }
    
    public String getChangePasswordRedirect() {
        return changePasswordRedirect;
    }
    
    public String getPasswordChangedMessage() {
        return passwordChangedMessage;
    }
    
}
