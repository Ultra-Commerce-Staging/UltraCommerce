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
package com.ultracommerce.core.rating.domain;

import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationToOneLookup;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.CustomerImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_RATING_DETAIL")
public class RatingDetailImpl implements RatingDetail, Serializable {

    @Id
    @GeneratedValue(generator = "RatingDetailId")
    @GenericGenerator(
        name="RatingDetailId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="RatingDetailImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.core.rating.domain.RatingDetailImpl")
        }
    )
    @Column(name = "RATING_DETAIL_ID")
    private Long id;

    @Column(name = "RATING", nullable = false)
    @AdminPresentation(friendlyName = "RatingDetail_rating", prominent = true)
    protected Double rating;

    @Column(name = "RATING_SUBMITTED_DATE", nullable = false)
    @AdminPresentation(friendlyName = "RatingDetail_submittedDate", prominent = true)
    protected Date ratingSubmittedDate;

    @ManyToOne(targetEntity = CustomerImpl.class, optional = false)
    @JoinColumn(name = "CUSTOMER_ID")
    @Index(name="RATING_CUSTOMER_INDEX", columnNames={"CUSTOMER_ID"})
    @AdminPresentation(friendlyName = "RatingDetail_customer", prominent = true)
    @AdminPresentationToOneLookup
    protected Customer customer;

    @ManyToOne(optional = false, targetEntity = RatingSummaryImpl.class)
    @JoinColumn(name = "RATING_SUMMARY_ID")
    @AdminPresentation(friendlyName = "RatingDetail_summary")
    @AdminPresentationToOneLookup
    protected RatingSummary ratingSummary;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Double getRating() {
        return rating;
    }

    @Override
    public void setRating(Double newRating) {
        this.rating = newRating;
    }

    @Override
    public Date getRatingSubmittedDate() {
        return ratingSubmittedDate;
    }

    @Override
    public void setRatingSubmittedDate(Date ratingSubmittedDate) {
        this.ratingSubmittedDate = ratingSubmittedDate;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public RatingSummary getRatingSummary() {
        return ratingSummary;
    }

    @Override
    public void setRatingSummary(RatingSummary ratingSummary) {
        this.ratingSummary = ratingSummary;
    }


}
