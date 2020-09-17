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
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.AdminPresentationCollection;
import com.ultracommerce.common.presentation.AdminPresentationToOneLookup;
import com.ultracommerce.common.presentation.PopulateToOneFieldsEnum;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.core.rating.service.type.ReviewStatusType;
import com.ultracommerce.profile.core.domain.Customer;
import com.ultracommerce.profile.core.domain.CustomerImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_REVIEW_DETAIL")
@AdminPresentationClass(friendlyName = "ReviewDetail", populateToOneFields = PopulateToOneFieldsEnum.TRUE)
public class ReviewDetailImpl implements ReviewDetail, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "ReviewDetailId")
    @GenericGenerator(
        name="ReviewDetailId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="ReviewDetailImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.core.rating.domain.ReviewDetailImpl")
        }
    )
    @Column(name = "REVIEW_DETAIL_ID")
    private Long id;

    @ManyToOne(targetEntity = CustomerImpl.class, optional = false)
    @JoinColumn(name = "CUSTOMER_ID")
    @Index(name="REVIEWDETAIL_CUSTOMER_INDEX", columnNames={"CUSTOMER_ID"})
    @AdminPresentationToOneLookup
    @AdminPresentation(friendlyName = "ReviewDetail_customer")
    protected Customer customer;

    @Column(name = "REVIEW_SUBMITTED_DATE", nullable = false)
    @AdminPresentation(friendlyName = "ReviewDetail_submittedDate")
    protected Date reivewSubmittedDate;

    @Column(name = "REVIEW_TEXT", nullable = false)
    @AdminPresentation(friendlyName = "ReviewDetail_reviewText", largeEntry = true)
    protected String reviewText;

    @Column(name = "REVIEW_STATUS", nullable = false)
    @Index(name="REVIEWDETAIL_STATUS_INDEX", columnNames={"REVIEW_STATUS"})
    @AdminPresentation(friendlyName = "ReviewDetail_status",
        prominent = true,
        fieldType = SupportedFieldType.ULTRA_ENUMERATION,
        ultraEnumeration = "com.ultracommerce.core.rating.service.type.ReviewStatusType")
    protected String reviewStatus;

    @Column(name = "HELPFUL_COUNT", nullable = false)
    @AdminPresentation(friendlyName = "ReviewDetail_helpfulCount")
    protected Integer helpfulCount;

    @Column(name = "NOT_HELPFUL_COUNT", nullable = false)
    @AdminPresentation(friendlyName = "ReviewDetail_notHelpfulCount")
    protected Integer notHelpfulCount;

    @ManyToOne(optional = false, targetEntity = RatingSummaryImpl.class)
    @JoinColumn(name = "RATING_SUMMARY_ID")
    @Index(name="REVIEWDETAIL_SUMMARY_INDEX", columnNames={"RATING_SUMMARY_ID"})
    protected RatingSummary ratingSummary;

    @OneToMany(mappedBy = "reviewDetail", targetEntity = ReviewFeedbackImpl.class, cascade = {CascadeType.ALL})
    @AdminPresentationCollection(friendlyName = "ReviewDetail_feedback")
    protected List<ReviewFeedback> reviewFeedback;

    @OneToOne(targetEntity = RatingDetailImpl.class)
    @JoinColumn(name = "RATING_DETAIL_ID")
    @Index(name="REVIEWDETAIL_RATING_INDEX", columnNames={"RATING_DETAIL_ID"})
    @AdminPresentation(friendlyName = "ReviewDetail_ratingDetail")
    @AdminPresentationToOneLookup
    protected RatingDetail ratingDetail;

    public ReviewDetailImpl() {}

    public ReviewDetailImpl(Customer customer, Date reivewSubmittedDate, RatingDetail ratingDetail, String reviewText, RatingSummary ratingSummary) {
        super();
        this.customer = customer;
        this.reivewSubmittedDate = reivewSubmittedDate;
        this.reviewText = reviewText;
        this.ratingSummary = ratingSummary;
        this.reviewFeedback = new ArrayList<ReviewFeedback>();
        this.helpfulCount = Integer.valueOf(0);
        this.notHelpfulCount = Integer.valueOf(0);
        this.reviewStatus = ReviewStatusType.PENDING.getType();
        this.ratingDetail = ratingDetail;
    }

    @Override
    public Date getReviewSubmittedDate() {
        return reivewSubmittedDate;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getReviewText() {
        return reviewText;
    }

    @Override
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    @Override
    public ReviewStatusType getStatus() {
        return new ReviewStatusType(reviewStatus);
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public Integer helpfulCount() {
        return helpfulCount;
    }

    @Override
    public Integer notHelpfulCount() {
        return notHelpfulCount;
    }

    @Override
    public RatingSummary getRatingSummary() {
        return ratingSummary;
    }

    @Override
    public RatingDetail getRatingDetail() {
        return ratingDetail;
    }

    @Override
    public List<ReviewFeedback> getReviewFeedback() {
        return reviewFeedback == null ? new ArrayList<ReviewFeedback>() : reviewFeedback;
    }

}
