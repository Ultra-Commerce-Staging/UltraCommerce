/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.common.security.service;

/**
 * This exception is thrown when the system detects a request from a "stale" page. This relates to a
 * Http Status 409 error response. See {@link StaleStateProtectionService} for more information.
 *
 * @author Jeff Fischer
 */
public class StaleStateServiceException extends RuntimeException {

    public StaleStateServiceException() {
    }

    public StaleStateServiceException(Throwable cause) {
        super(cause);
    }

    public StaleStateServiceException(String message) {
        super(message);
    }

    public StaleStateServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
