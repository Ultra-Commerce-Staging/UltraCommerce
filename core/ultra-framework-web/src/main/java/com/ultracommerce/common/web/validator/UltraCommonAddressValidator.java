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
package com.ultracommerce.common.web.validator;

import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.common.web.form.UltraFormType;
import com.ultracommerce.profile.core.domain.Address;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import javax.annotation.Resource;

/**
 * Generic Address Validator not specific to a particular Country.
 * Some modules may provide custom validation which can be enabled by setting
 * the {@link com.ultracommerce.common.config.domain.SystemProperty} "validator.custom.enabled"
 *
 * If a custom validation is not performed, a generic set of address validation is used.
 * You may configure this validator to only validate a Full Name rather than a separate first and last name,
 * by setting the {@link com.ultracommerce.common.config.domain.SystemProperty} "validator.address.fullNameOnly"
 *
 * @author Elbert Bautista (elbertbautista)
 */
public class UltraCommonAddressValidator {

    @Resource(name = "ucUltraCommonAddressValidatorExtensionManager")
    protected UltraCommonAddressValidatorExtensionManager validatorExtensionManager;

    public boolean isValidateFullNameOnly() {
        return UCSystemProperty.resolveBooleanSystemProperty("validator.address.fullNameOnly");
    }

    public boolean isCustomValidationEnabled() {
        return UCSystemProperty.resolveBooleanSystemProperty("validator.custom.enabled");
    }

    public void validate(UltraFormType formType, Address address, Errors errors) {
        if (isCustomValidationEnabled())  {
            validatorExtensionManager.getProxy().validate(formType, address, errors);
            return;
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.addressLine1", "addressLine1.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.city", "city.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.stateProvinceRegion", "state.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.postalCode", "postalCode.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.phonePrimary.phoneNumber", "phonePrimary.required");

        if (isValidateFullNameOnly()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.fullName", "fullName.required");
        } else {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.firstName", "firstName.required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.lastName", "lastName.required");
        }

        if (address.getIsoCountryAlpha2() == null && address.getCountry() == null) {
            errors.rejectValue("address.isoCountryAlpha2", "country.required", null, null);
        }
    }

}
