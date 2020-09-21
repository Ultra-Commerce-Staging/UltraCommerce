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

import com.ultracommerce.common.web.form.UltraFormType;
import com.ultracommerce.common.web.validator.UltraCommonAddressValidator;
import com.ultracommerce.core.web.controller.account.CustomerAddressForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("ucCustomerAddressValidator")
public class CustomerAddressValidator extends UltraCommonAddressValidator implements Validator {

    @SuppressWarnings("rawtypes")
    public boolean supports(Class clazz) {
        return clazz.equals(CustomerAddressValidator.class);
    }

    public void validate(Object obj, Errors errors) {
        CustomerAddressForm form = (CustomerAddressForm) obj;
        super.validate(UltraFormType.CUSTOMER_ADDRESS_FORM, form.getAddress(), errors);
    }
}

