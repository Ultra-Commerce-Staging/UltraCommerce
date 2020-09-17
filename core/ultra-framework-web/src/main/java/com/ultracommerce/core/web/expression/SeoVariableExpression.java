/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.expression;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.page.dto.PageDTO;
import com.ultracommerce.common.web.expression.UltraVariableExpression;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.web.seo.SeoPropertyService;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Service;

import java.util.Map;

import javax.annotation.Resource;

@Service("ucSeoVariableExpression")
@ConditionalOnTemplating
public class SeoVariableExpression implements UltraVariableExpression {

    private static final Log LOG = LogFactory.getLog(SeoVariableExpression.class);

    @Resource(name = "ucSeoPropertyService")
    protected SeoPropertyService seoPropertyService;

    @Override
    public String getName() {
        return "seo";
    }


    public Map<String, String> getMetaProperties(Category category) {
        return seoPropertyService.getSeoProperties(category);
    }

    public Map<String, String> getMetaProperties(Product product) {
        return seoPropertyService.getSeoProperties(product);
    }

    public Map<String, String> getMetaProperties(PageDTO page) {
        return seoPropertyService.getSeoProperties(page);
    }

}
