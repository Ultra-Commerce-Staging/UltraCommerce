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
 * The admin will throw this exception when a query is attempted across multiple class hierarchies
 * because it is impossible for such a query to produce any results.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class NoPossibleResultsException extends RuntimeException {
    
    private static final long serialVersionUID = 2422275745139590462L;

    // for serialization purposes
    protected NoPossibleResultsException() {
        super();
    }
    
    public NoPossibleResultsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public NoPossibleResultsException(String message) {
        super(message);
    }
    
    public NoPossibleResultsException(Throwable cause) {
        super(cause);
    }

}
