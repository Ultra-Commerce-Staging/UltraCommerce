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
 * Base exception class for UltraExceptions that understands root cause messages.
 * 
 * @author bpolster
 */
public abstract class UltraException extends Exception implements RootCauseAccessor {

    private Throwable rootCause;

    public UltraException() {
        super();
    }

    public UltraException(String message, Throwable cause) {
        super(message, cause);
        if (cause != null) {
            rootCause = findRootCause(cause);
        } else {
            rootCause = this;
        }
    }

    private Throwable findRootCause(Throwable cause) {
        Throwable rootCause = cause;
        while (rootCause != null && rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

    public UltraException(String message) {
        super(message);
        this.rootCause = this;

    }

    public UltraException(Throwable cause) {
        super(cause);
        if (cause != null) {
            rootCause = findRootCause(cause);
        }
    }

    public Throwable getRootCause() {
        return rootCause;
    }

    public String getRootCauseMessage() {
        if (rootCause != null) {
            return rootCause.getMessage();
        } else {
            return getMessage();
        }
    }

}
