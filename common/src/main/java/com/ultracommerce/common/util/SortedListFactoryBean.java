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
package com.ultracommerce.common.util;

import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.core.Ordered;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Jeff Fischer
 */
public class SortedListFactoryBean extends ListFactoryBean {

    @Override
    protected List createInstance() {
        List response = super.createInstance();
        Collections.sort(response, new Comparator<Ordered>() {
            @Override
            public int compare(Ordered o1, Ordered o2) {
                return new Integer(o1.getOrder()).compareTo(o2.getOrder());
            }
        });

        return response;
    }
}
