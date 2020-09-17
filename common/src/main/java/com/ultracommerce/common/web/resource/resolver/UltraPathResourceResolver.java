/*
 * #%L
 * ultra-theme
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
package com.ultracommerce.common.web.resource.resolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * Wraps Spring's {@link PathResourceResolver} for ordering purposes.
 *  
 * @author Brian Polster
 * @since Ultra 4.0
 */
@Component("ucPathResourceResolver")
public class UltraPathResourceResolver extends PathResourceResolver implements Ordered {

    protected static final Log LOG = LogFactory.getLog(UltraPathResourceResolver.class);

    private int order = UltraResourceResolverOrder.UC_PATH_RESOURCE_RESOLVER;

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


}
