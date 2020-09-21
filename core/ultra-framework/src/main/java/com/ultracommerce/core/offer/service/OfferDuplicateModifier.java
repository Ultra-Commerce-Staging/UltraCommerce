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
package com.ultracommerce.core.offer.service;

import com.ultracommerce.common.copy.MultiTenantCloneable;
import com.ultracommerce.common.persistence.AbstractEntityDuplicationHelper;
import com.ultracommerce.core.offer.domain.Offer;
import com.ultracommerce.core.offer.domain.OfferImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Modify new Offer duplicates before persistence
 *
 * @author Jeff Fischer
 */
@Component("ucOfferDuplicateModifier")
public class OfferDuplicateModifier extends AbstractEntityDuplicationHelper<Offer> {

    @Autowired
    public OfferDuplicateModifier(final Environment environment) {
        super(environment);
        
        addCopyHint(OfferImpl.EXCLUDE_OFFERCODE_COPY_HINT, Boolean.TRUE.toString());
    }
    
    @Override
    public boolean canHandle(final MultiTenantCloneable candidate) {
        return Offer.class.isAssignableFrom(candidate.getClass());
    }
    
    @Override
    public void modifyInitialDuplicateState(final Offer copy) {
        String currentName = copy.getName();
        copy.setName(currentName + getCopySuffix());
        copy.setStartDate(null);
        copy.setEndDate(null);
    }
}
