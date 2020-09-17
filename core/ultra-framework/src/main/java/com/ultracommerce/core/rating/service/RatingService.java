/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.rating.service;

import com.ultracommerce.core.rating.domain.RatingSummary;
import com.ultracommerce.core.rating.domain.ReviewDetail;
import com.ultracommerce.core.rating.service.type.RatingSortType;
import com.ultracommerce.core.rating.service.type.RatingType;
import com.ultracommerce.profile.core.domain.Customer;

import java.util.List;
import java.util.Map;

public interface RatingService {

    public RatingSummary saveRatingSummary(RatingSummary rating);
    public void deleteRatingSummary(RatingSummary rating);
    public RatingSummary readRatingSummary(String itemId, RatingType type);
    public Map<String, RatingSummary> readRatingSummaries(List<String> itemIds, RatingType type);
    public void rateItem(String itemId, RatingType type, Customer customer, Double rating);

    public List<ReviewDetail> readReviews(String itemId, RatingType type, int start, int finish, RatingSortType sortBy);
    public void reviewItem(String itemId, RatingType type, Customer customer, Double rating, String reviewText);
    public void markReviewHelpful(Long reviewId, Customer customer, Boolean helpful);
    
    /**
     * Reads a ReviewDetail by the given customer and the itemId
     * @param itemId
     * @param customer
     * @return review, or null if review is not found
     */
    public ReviewDetail readReviewByCustomerAndItem(Customer customer, String itemId);

}
