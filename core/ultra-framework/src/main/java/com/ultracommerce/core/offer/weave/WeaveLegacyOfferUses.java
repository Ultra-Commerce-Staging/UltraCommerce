/*
 * #%L
 * ultra-enterprise
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
package com.ultracommerce.core.offer.weave;

import com.ultracommerce.core.offer.domain.LegacyOfferUsesImpl;

import javax.persistence.Embedded;

/**
 * Provide a template class that holds interfaces, fields and methods to be optionally, dynamically introduced
 * into OfferImpl at runtime.
 *
 * @see LegacyOfferUses
 * @author Jeff Fischer
 */
public abstract class WeaveLegacyOfferUses implements LegacyOfferUses {

    @Embedded
    protected LegacyOfferUsesImpl embeddableLegacyOfferUses;

    @Override
    public int getMaxUses() {
        return getEmbeddableLegacyOfferUses(false).getMaxUses();
    }

    @Override
    public int getUses() {
        return getEmbeddableLegacyOfferUses(false).getUses();
    }

    @Override
    public void setUses(int uses) {
        getEmbeddableLegacyOfferUses(true).setUses(uses);
    }

    @Override
    public void setMaxUses(int maxUses) {
        getEmbeddableLegacyOfferUses(true).setMaxUses(maxUses);
    }

    protected LegacyOfferUsesImpl getEmbeddableLegacyOfferUses(boolean assign) {
        LegacyOfferUsesImpl temp = embeddableLegacyOfferUses;
        if (temp == null) {
            temp = new LegacyOfferUsesImpl();
            if (assign) {
                embeddableLegacyOfferUses = temp;
            }
        }
        return temp;
    }

}
