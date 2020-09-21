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
package com.ultracommerce.core.web.controller.account.validator;

import com.ultracommerce.common.security.util.PasswordChange;
import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.profile.core.service.CustomerService;
import com.ultracommerce.profile.web.core.CustomerState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

@Component("ucChangePasswordValidator")
public class ChangePasswordValidator implements Validator {

    private static final String DEFAULT_VALID_PASSWORD_REGEX = "[^\\s]{6,}";

    @Resource(name = "ucCustomerService")
    protected CustomerService customerService;

    @Value("${enable.current.password.check:true}")
    protected Boolean enableCurrentPasswordCheck = true;

    public void validate(PasswordChange passwordChange, Errors errors) {

        String currentPassword = passwordChange.getCurrentPassword();
        String password = passwordChange.getNewPassword();
        String passwordConfirm = passwordChange.getNewPasswordConfirm();

        if (enableCurrentPasswordCheck) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currentPassword", "currentPassword.required");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "newPassword.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPasswordConfirm", "newPasswordConfirm.required");

        if (!errors.hasErrors()) {
            //validate current password
            if (enableCurrentPasswordCheck && !customerService.isPasswordValid(currentPassword, CustomerState.getCustomer().getPassword())) {
                errors.rejectValue("currentPassword", "currentPassword.invalid");
            }
            //password and confirm password fields must be equal
            if (!passwordConfirm.equals(password)) {
                errors.rejectValue("newPasswordConfirm", "newPasswordConfirm.invalid");
            }
            //restrict password characteristics
            if (!password.matches(getValidPasswordRegex())) {
                errors.rejectValue("newPassword", "newPassword.invalid");
            }
        }

    }

    public static String getValidPasswordRegex() {
        return UCSystemProperty.resolveSystemProperty("password.valid.regex", DEFAULT_VALID_PASSWORD_REGEX);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {}

}
