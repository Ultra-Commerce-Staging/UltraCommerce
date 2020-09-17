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

package com.ultracommerce.core.inventory.service;


public class InventoryUnavailableException extends Exception {

    private static final long serialVersionUID = 1L;

    protected Long skuId;

    protected Integer quantityRequested;

    protected Integer quantityAvailable;

    public InventoryUnavailableException(String msg) {
        super(msg);
    }
    
    public InventoryUnavailableException(Long skuId, Integer quantityRequested, Integer quantityAvailable) {
        super();
        this.skuId = skuId;
        this.quantityAvailable = quantityAvailable;
        this.quantityRequested = quantityRequested;
    }

    public InventoryUnavailableException(String arg0, Long skuId, Integer quantityRequested, Integer quantityAvailable) {
        super(arg0);
        this.skuId = skuId;
        this.quantityAvailable = quantityAvailable;
        this.quantityRequested = quantityRequested;
    }
    
    public InventoryUnavailableException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public int getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(int quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

}
