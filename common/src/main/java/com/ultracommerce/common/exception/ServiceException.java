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

/**
 * Exception thrown when a {@link EntityService service} method fails.
 * 
 * @author jfischer
 */
public class ServiceException extends Exception {
    
    private static final long serialVersionUID = -7084792578727995587L;
    
    // for serialization purposes
    protected ServiceException() {
        super();
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Checks to see if any of the causes of the chain of exceptions that led to this ServiceException are an instance
     * of the given class.
     * 
     * @param clazz
     * @return whether or not this exception's causes includes the given class.
     */
    public boolean containsCause(Class<? extends Throwable> clazz) {
        Throwable current = this;

        do {
            if (clazz.isAssignableFrom(current.getClass())) {
                return true;
            }
            current = current.getCause();
        } while (current != null && current.getCause() != null);

        return false;
    }

}
