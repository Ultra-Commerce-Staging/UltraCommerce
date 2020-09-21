/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.common.event;

import java.io.Serializable;

/**
 * Effectively a copy of com.ultracommerce.jobsevents.domain.dto.SystemEventDetailDTO
 * to be used when creating a com.ultracommerce.common.event.UltraSystemEvent
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public class UltraSystemEventDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String friendlyName;
    protected String value;
    protected Serializable blob;

    public UltraSystemEventDetail(String friendlyName, String value) {
        this.friendlyName = friendlyName;
        this.value = value;
    }

    public UltraSystemEventDetail(String friendlyName, Serializable blob) {
        this.friendlyName = friendlyName;
        this.blob = blob;
    }

    public UltraSystemEventDetail(String value) {
        this.value = value;
    }

    public UltraSystemEventDetail(Serializable blob) {
        this.blob = blob;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Serializable getBlob() {
        return blob;
    }

    public void setBlob(Serializable blob) {
        this.blob = blob;
    }
}
