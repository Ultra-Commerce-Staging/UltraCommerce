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
package com.ultracommerce.profile.core.service.exception;


public class AddressVerificationException extends Exception {

    private static final long serialVersionUID = 1L;

    public AddressVerificationException() {
        super();
    }

    public AddressVerificationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public AddressVerificationException(String arg0) {
        super(arg0);
    }

    public AddressVerificationException(Throwable arg0) {
        super(arg0);
    }

}
