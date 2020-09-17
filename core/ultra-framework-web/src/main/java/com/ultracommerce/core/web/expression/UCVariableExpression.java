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

package com.ultracommerce.core.web.expression;

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.currency.util.UltraCurrencyUtils;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.common.util.StringUtil;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.common.web.expression.UltraVariableExpression;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.service.CatalogURLService;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;

import javax.annotation.Resource;

/**
 * Exposes "blc" to expressions to the Thymeleaf expression context.
 * 
 * This class is intended to be augmented using load time weaving by other modules
 * within Ultra.
 * 
 * It provides one function (getDate()) primarily just for testing purposes.   This can
 * be accessed with Thymeleaf as ${#blc.date()}
 * 
 * @author bpolster
 */
@Component("ucUCVariableExpression")
@ConditionalOnTemplating
public class UCVariableExpression implements UltraVariableExpression {
    
    @Override
    public String getName() {
        return "blc";
    }
    
    @Resource(name = "ucCatalogURLService")
    protected CatalogURLService catalogURLService;

    public String relativeURL(Category category) {
        return catalogURLService.buildRelativeCategoryURL(getCurrentUrl(), category);
    }

    public String relativeURL(Product product) {
        return catalogURLService.buildRelativeProductURL(getCurrentUrl(), product);
    }

    public String relativeURL(String baseUrl, Category category) {
        return catalogURLService.buildRelativeCategoryURL(baseUrl, category);
    }

    public String relativeURL(String baseUrl, Product product) {
        return catalogURLService.buildRelativeProductURL(baseUrl, product);
    }

    protected String getCurrentUrl() {
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        String currentUrl = "";
        if (brc != null && brc.getRequest() != null) {
            currentUrl = brc.getRequest().getRequestURI();

            if (!StringUtils.isEmpty(brc.getRequest().getQueryString())) {
                currentUrl = currentUrl + "?" + brc.getRequest().getQueryString();
            }
        }
        return currentUrl;
    }

    /**
     * Returns the price at the correct scale and rounding for the default currency
     * @see Money#defaultCurrency()
     * @param amount
     * @return
     */
    public String getPrice(String amount) {
        Money price = Money.ZERO;
        String sanitizedAmount = StringUtil.removeNonNumerics(amount);
        if (StringUtils.isNotEmpty(sanitizedAmount)) {
            price = new Money(sanitizedAmount);
            UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
            if (brc.getJavaLocale() != null) {
                NumberFormat formatter = UltraCurrencyUtils.getNumberFormatFromCache(brc.getJavaLocale(), price.getCurrency());
                return formatter.format(price.getAmount());
            }
        }
        return "$ " + price.getAmount().toString();
    }
}
