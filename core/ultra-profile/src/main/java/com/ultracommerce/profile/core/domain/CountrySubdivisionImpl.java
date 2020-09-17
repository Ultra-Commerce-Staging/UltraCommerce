/*
 * #%L
 * UltraCommerce Profile
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
package com.ultracommerce.profile.core.domain;

import com.ultracommerce.common.admin.domain.AdminMainEntity;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.i18n.service.DynamicTranslationProvider;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_COUNTRY_SUB")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucStandardElements")
@AdminPresentationClass(friendlyName = "CountrySubdivisionImpl_baseSubdivision")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.AUDITABLE_ONLY)
})
public class CountrySubdivisionImpl implements CountrySubdivision, AdminMainEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ABBREVIATION")
    protected String abbreviation;

    @Column(name = "NAME", nullable = false)
    @Index(name="COUNTRY_SUB_NAME_IDX", columnNames={"NAME"})
    @AdminPresentation(friendlyName = "CountrySubdivisionImpl_Name", order=9, group = "CountrySubdivisionImpl_Address", prominent = true, translatable = true)
    protected String name;

    @Column(name = "ALT_ABBREVIATION")
    @Index(name="COUNTRY_SUB_ALT_ABRV_IDX", columnNames={"ALT_ABBREVIATION"})
    @AdminPresentation(friendlyName = "CountrySubdivisionImpl_AltAbbreviation", order=10, group = "CountrySubdivisionImpl_Address", prominent = true)
    protected String alternateAbbreviation;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = CountryImpl.class, optional = false)
    @JoinColumn(name = "COUNTRY")
    protected Country country;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, targetEntity = CountrySubdivisionCategoryImpl.class)
    @JoinColumn(name = "COUNTRY_SUB_CAT")
    protected CountrySubdivisionCategory category;

    @Override
    public String getAbbreviation() {
        return abbreviation;
    }

    @Override
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String getAlternateAbbreviation() {
        return alternateAbbreviation;
    }

    @Override
    public void setAlternateAbbreviation(String alternateAbbreviation) {
        this.alternateAbbreviation = alternateAbbreviation;
    }

    @Override
    public String getName() {
        return DynamicTranslationProvider.getValue(this, "name", name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Country getCountry() {
        return country;
    }

    @Override
    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public CountrySubdivisionCategory getCategory() {
        return category;
    }

    @Override
    public void setCategory(CountrySubdivisionCategory category) {
        this.category = category;
    }

    @Override
    public String getMainEntityName() {
        return getName();
    }
}
