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
package com.ultracommerce.common.extensibility.context.merge;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * This {@link MergeBeanStatusProvider} can be utilized by modules that are trying to provide functionality that 
 * is only required when MultiTenant is loaded.
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Component("ucMultiTenantMergeBeanStatusProvider")
public class MultiTenantMergeBeanStatusProvider implements MergeBeanStatusProvider {

    @Override
    public boolean isProcessingEnabled(Object bean, String beanName, ApplicationContext appCtx) {
        return appCtx.containsBean("ucMultiTenantFilterClassTransformer");
    }

}
