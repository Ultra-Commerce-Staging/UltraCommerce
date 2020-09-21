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
package com.ultracommerce.common.web.resource.transformer;

import com.ultracommerce.common.resource.service.ResourceMinificationService;
import com.ultracommerce.common.web.resource.resolver.UltraResourceTransformerOrder;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceTransformer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

/**
 * A {@link org.springframework.web.servlet.resource.ResourceTransformer} that minifies
 * the resource.    Only works with allowed extensions (".css" and ".js" by default).
 * 
 * {@link com.ultracommerce.common.resource.service.ResourceMinificationService} is used to
 * perform the minification. 
 *
 * @author Brian Polster
 * @since 4.0
 */
@Component("ucMinifyResourceTransformer")
public class MinifyResourceTransformer implements ResourceTransformer, Ordered {

    @javax.annotation.Resource(name = "ucResourceMinificationService")
    protected ResourceMinificationService minifyService;

    private int order = UltraResourceTransformerOrder.UC_MINIFY_RESOURCE_TRANSFORMER;

    @Override
    public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain)
            throws IOException {

        Resource transformed = transformerChain.transform(request, resource);

        return minifyService.minify(transformed);
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
