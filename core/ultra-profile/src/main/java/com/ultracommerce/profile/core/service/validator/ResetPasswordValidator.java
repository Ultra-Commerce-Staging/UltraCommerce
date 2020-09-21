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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("ucResetPasswordValidator")
public class ResetPasswordValidator implements Validator {
    public void validate(String username, String password, String confirmPassword, Errors errors) {
        if (StringUtils.isEmpty(password)) {
            errors.reject("password", "password.required");
        }
        
        if (StringUtils.isEmpty(username)) {
            errors.reject("username", "username.required");
        }
        
        if (! errors.hasErrors()) {
            if (! password.matches(RegistrationValidator.getValidPasswordRegex())) {
                errors.rejectValue("password", "password.invalid", null, null);
            } else {
                if (!password.equals(confirmPassword)) {
                    errors.rejectValue("password", "passwordConfirm.invalid", null, null);
                }
            }        
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {}
}
