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
package com.ultracommerce.core.promotionMessage.domain;

import com.ultracommerce.common.copy.MultiTenantCloneable;
import com.ultracommerce.common.locale.domain.Locale;
import com.ultracommerce.common.media.domain.Media;
import com.ultracommerce.common.persistence.Status;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Chris Kittrell (ckittrell)
 */
public interface PromotionMessage extends Status, Serializable,MultiTenantCloneable<PromotionMessage> {

    public void setId(Long id);

    public Long getId();

    public String getName();

    public void setName(String name);

    public String getMessage();

    public void setMessage(String message);

    public Media getMedia();

    public void setMedia(Media media);

    public int getPriority();

    public void setPriority(Integer priority);

    public Date getStartDate();

    public void setStartDate(Date startDate);

    public Date getEndDate();

    public void setEndDate(Date endDate);

    public String getMessagePlacement();

    public void setMessagePlacement(String messagePlacement);

    public Locale getLocale();

    public void setLocale(Locale locale);

}
