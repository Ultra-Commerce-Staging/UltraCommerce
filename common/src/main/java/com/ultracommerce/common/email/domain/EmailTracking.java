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
public interface EmailTracking extends Serializable {

    public abstract Long getId();

    public abstract void setId(Long id);

    /**
     * @return the emailAddress
     */
    public abstract String getEmailAddress();

    /**
     * @param emailAddress the emailAddress to set
     */
    public abstract void setEmailAddress(String emailAddress);

    /**
     * @return the dateSent
     */
    public abstract Date getDateSent();

    /**
     * @param dateSent the dateSent to set
     */
    public abstract void setDateSent(Date dateSent);

    /**
     * @return the type
     */
    public abstract String getType();

    /**
     * @param type the type to set
     */
    public abstract void setType(String type);

}
