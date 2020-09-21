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
package com.ultracommerce.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Form of ServiceException thrown when their is a security
 * failure while attempting to execute the operation.
 *
 * @author Jeff Fischer
 */
@ResponseStatus(value= HttpStatus.FORBIDDEN, reason="Access is denied")
public class SecurityServiceException extends ServiceException {

    public SecurityServiceException() {
        super();
    }

    public SecurityServiceException(Throwable cause) {
        super(cause);
    }

    public SecurityServiceException(String message) {
        super(message);
    }

    public SecurityServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
