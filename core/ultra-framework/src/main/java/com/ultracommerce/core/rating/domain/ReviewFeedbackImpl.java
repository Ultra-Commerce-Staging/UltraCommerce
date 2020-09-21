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
@Table(name = "UC_REVIEW_FEEDBACK")
public class ReviewFeedbackImpl implements ReviewFeedback, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "ReviewFeedbackId")
    @GenericGenerator(
        name="ReviewFeedbackId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="ReviewFeedbackImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.core.rating.domain.ReviewFeedbackImpl")
        }
    )
    @Column(name = "REVIEW_FEEDBACK_ID")
    protected Long id;

    @ManyToOne(targetEntity = CustomerImpl.class, optional = false)
    @JoinColumn(name = "CUSTOMER_ID")
    @Index(name="REVIEWFEED_CUSTOMER_INDEX", columnNames={"CUSTOMER_ID"})
    @AdminPresentation(friendlyName="ReviewFeedback_customer")
    @AdminPresentationToOneLookup
    protected Customer customer;

    @Column(name = "IS_HELPFUL", nullable = false)
    @AdminPresentation(friendlyName = "ReviewFeedback_isHelpful")
    protected Boolean isHelpful = false;

    @ManyToOne(optional = false, targetEntity = ReviewDetailImpl.class)
    @JoinColumn(name = "REVIEW_DETAIL_ID")
    @Index(name="REVIEWFEED_DETAIL_INDEX", columnNames={"REVIEW_DETAIL_ID"})
    @AdminPresentationToOneLookup
    @AdminPresentation(friendlyName = "ReviewFeedback_reviewDetail")
    protected ReviewDetail reviewDetail;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public ReviewDetail getReviewDetail() {
        return reviewDetail;
    }

    @Override
    public Boolean getIsHelpful() {
        return isHelpful;
    }

    @Override
    public void setIsHelpful(Boolean isHelpful) {
        this.isHelpful = isHelpful;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void setReviewDetail(ReviewDetail reviewDetail) {
        this.reviewDetail = reviewDetail;
    }

}
