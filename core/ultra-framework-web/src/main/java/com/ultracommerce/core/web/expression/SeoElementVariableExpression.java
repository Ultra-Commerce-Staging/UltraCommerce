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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.util.UCMessageUtils;
import com.ultracommerce.common.web.expression.UltraVariableExpression;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service("ucSeoElementVariableExpression")
@ConditionalOnTemplating
public class SeoElementVariableExpression implements UltraVariableExpression {

    private static final Log LOG = LogFactory.getLog(SeoElementVariableExpression.class);

    @Override
    public String getName() {
        return "seoElement";
    }


    public String getSiteSimpleURL() {
        return UCMessageUtils.getMessage("seo.site.simple.url");
    }

    public String getTitle(Category category) {
        String title = category.getMetaTitle();

        if (StringUtils.isEmpty(title)) {
            title = category.getName();
        }

        return title;
    }

    public String getDescription(Category category) {
        String result = "";

        String metaDescription = category.getMetaDescription();
        String description = category.getLongDescription();

        if (StringUtils.isNotEmpty(metaDescription)) {
            result = metaDescription;
        } else if (StringUtils.isNotEmpty(description)) {
            result = description;
        }

        return result;
    }

    public String getTitle(Product product) {
        String title = product.getMetaTitle();

        if (StringUtils.isEmpty(title)) {
            title = product.getName();
        }

        return title;
    }

    public String getDescription(Product product) {
        String result = "";

        String metaDescription = product.getMetaDescription();
        String description = product.getLongDescription();

        if (StringUtils.isNotEmpty(metaDescription)) {
            result = metaDescription;
        } else if (StringUtils.isNotEmpty(description)) {
            result = description;
        }

        return result;
    }

    public String buildTitleString(List<String> titleElements, String elementDelimiter) {
        titleElements.removeAll(Arrays.asList(null, ""));
        String result = StringUtils.join(titleElements, elementDelimiter);
        return StringUtils.isNotEmpty(result) ? result : getSiteSimpleURL();
    }
}
