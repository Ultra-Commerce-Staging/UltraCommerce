/*
 * #%L
 * UltraCommerce Framework
 * %%
 * Copyright (C) 2009 - 2018 Ultra Commerce
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

public class MinQuantityNotFulfilledException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    protected Long productId;

    public MinQuantityNotFulfilledException (String message, Long productId) {
        super(message);
        setProductId(productId);
    }

    public MinQuantityNotFulfilledException(String message) {
        super(message);
    }

    public MinQuantityNotFulfilledException(String message, Throwable cause) {
        super(message, cause);
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
