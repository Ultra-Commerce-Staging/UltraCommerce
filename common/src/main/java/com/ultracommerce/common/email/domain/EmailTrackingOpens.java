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
package com.ultracommerce.common.email.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jfischer
 *
 */
public interface EmailTrackingOpens extends Serializable {

    /**
     * @return the id
     */
    public abstract Long getId();

    /**
     * @param id the id to set
     */
    public abstract void setId(Long id);

    /**
     * @return the dateOpened
     */
    public abstract Date getDateOpened();

    /**
     * @param dateOpened the dateOpened to set
     */
    public abstract void setDateOpened(Date dateOpened);

    /**
     * @return the userAgent
     */
    public abstract String getUserAgent();

    /**
     * @param userAgent the userAgent to set
     */
    public abstract void setUserAgent(String userAgent);

    /**
     * @return the emailTracking
     */
    public abstract EmailTracking getEmailTracking();

    /**
     * @param emailTracking the emailTracking to set
     */
    public abstract void setEmailTracking(EmailTracking emailTracking);

}
