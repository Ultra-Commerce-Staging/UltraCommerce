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
package com.ultracommerce.profile.web.controller.validator;

import com.ultracommerce.profile.core.domain.Phone;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("ucPhoneValidator")
public class PhoneValidator implements Validator {

    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return clazz.equals(Phone.class);
    }

    public void validate(Object obj, Errors errors) {
        //use regular phone
        Phone phone = (Phone) obj;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phoneNumber", "phone.required",
                new Object[]{phone});

        if (!errors.hasErrors()) {
            String phoneNumber = phone.getPhoneNumber();
            String newString = phoneNumber.replaceAll("\\D", "");

            if (newString.length() != 10) {
                errors.rejectValue("phoneNumber", "phone.ten_digits_required", null);
            }

            // Check for common false data.
            if (newString.equals("1234567890") || newString.equals("0123456789") || newString.matches("0{10}") || newString.matches("1{10}") ||
                    newString.matches("2{10}") || newString.matches("3{10}") || newString.matches("4{10}") || newString.matches("5{10}") ||
                    newString.matches("6{10}") || newString.matches("7{10}") || newString.matches("8{10}") || newString.matches("9{10}")) {
                errors.rejectValue("phoneNumber", "phone.invalid", null);
            }
        }
    }
}
