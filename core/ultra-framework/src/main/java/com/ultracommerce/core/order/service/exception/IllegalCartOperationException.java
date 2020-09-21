/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.order.service.exception;


public abstract class IllegalCartOperationException extends RuntimeException {

    private static final long serialVersionUID = 5113456015951023947L;

    protected IllegalCartOperationException() {
        super();
    }
    
    public IllegalCartOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public IllegalCartOperationException(String message) {
        super(message);
    }
    
    public IllegalCartOperationException(Throwable cause) {
        super(cause);
    }
    
    public abstract String getType();

}
