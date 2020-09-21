/*
 * #%L
 * UltraCommerce Profile
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
package com.ultracommerce.profile.core.service.validator;

import org.apache.commons.validator.GenericValidator;
import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.profile.core.domain.Customer;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("ucRegistrationValidator")
public class RegistrationValidator implements Validator {

    private static final String DEFAULT_VALID_NAME_REGEX = "[A-Za-z'. -]{1,80}";

    private static final String DEFAULT_VALID_PASSWORD_REGEX = "[^\\s]{6,}";

    public void validate(Customer customer, String password, String passwordConfirm, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "passwordConfirm.required");
        errors.pushNestedPath("customer");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", "emailAddress.required");
        errors.popNestedPath();

        if (errors.hasErrors()){
            if (!passwordConfirm.equals(password)) {
                errors.rejectValue("passwordConfirm", "invalid"); 
            }
            
            if (!customer.getFirstName().matches(getValidNameRegex())) {
                errors.rejectValue("firstName", "firstName.invalid", null, null);
            }

            if (!customer.getLastName().matches(getValidNameRegex())) {
                errors.rejectValue("lastName", "lastName.invalid", null, null);
            }

            if (!customer.getPassword().matches(getValidPasswordRegex())) {
                errors.rejectValue("password", "password.invalid", null, null);
            }

            if (!password.equals(passwordConfirm)) {
                errors.rejectValue("password", "passwordConfirm.invalid", null, null);
            }

            if (!GenericValidator.isEmail(customer.getEmailAddress())) {
                errors.rejectValue("emailAddress", "emailAddress.invalid", null, null);
            }
        }
    }

    public static String getValidNameRegex() {
        return UCSystemProperty.resolveSystemProperty("name.valid.regex", DEFAULT_VALID_NAME_REGEX);
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
