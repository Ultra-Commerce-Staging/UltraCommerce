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

package com.ultracommerce.core.web.processor;

import org.apache.commons.lang.StringUtils;
import com.ultracommerce.core.rating.domain.RatingSummary;
import com.ultracommerce.core.rating.domain.ReviewDetail;
import com.ultracommerce.core.rating.service.RatingService;
import com.ultracommerce.core.rating.service.type.RatingType;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraVariableModifierProcessor;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * A Thymeleaf processor that will add the product ratings and reviews to the model
 *
 * @author jfridye
 */
@Component("ucRatingsProcessor")
@ConditionalOnTemplating
public class RatingsProcessor extends AbstractUltraVariableModifierProcessor {

    @Resource(name = "ucRatingService")
    protected RatingService ratingService;

    @Override
    public String getName() {
        return "ratings";
    }
    
    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    public Map<String, Object> populateModelVariables(String tagName, Map<String, String> tagAttributes, UltraTemplateContext context) {
        Object obj = context.parseExpression(tagAttributes.get("itemId"));
        String itemId = obj.toString();
        RatingSummary ratingSummary = ratingService.readRatingSummary(itemId, RatingType.PRODUCT);
        Map<String, Object> newModelVars = new HashMap<>();
        if (ratingSummary != null) {
            newModelVars.put(getRatingsVar(tagAttributes), ratingSummary);
        }

        Customer customer = CustomerState.getCustomer();
        ReviewDetail reviewDetail = null;
        if (!customer.isAnonymous()) {
            reviewDetail = ratingService.readReviewByCustomerAndItem(customer, itemId);
        }
        if (reviewDetail != null) {
            newModelVars.put("currentCustomerReview", reviewDetail);
        }
        return newModelVars;
    }

    private String getRatingsVar(Map<String, String> tagAttributes) {
        String ratingsVar = tagAttributes.get("ratingsVar");
        if (StringUtils.isNotEmpty(ratingsVar)) {
            return ratingsVar;
        }
        return "ratingSummary";
    }
}
