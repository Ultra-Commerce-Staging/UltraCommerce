/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
 * To be thrown when an optimistic lock is being used to update an entity and the maximum retry count is reached,
 * causing the update to be aborted.
 *
 * @author Philip Baggett (pbaggett)
 */
public class OptimisticLockMaxRetryException extends RuntimeException {
    public OptimisticLockMaxRetryException() {
        super();
    }

    public OptimisticLockMaxRetryException(String message) {
        super(message);
    }

    public OptimisticLockMaxRetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptimisticLockMaxRetryException(Throwable cause) {
        super(cause);
    }

    public OptimisticLockMaxRetryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
