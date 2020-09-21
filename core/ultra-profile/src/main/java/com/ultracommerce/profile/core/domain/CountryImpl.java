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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_COUNTRY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucStandardElements")
@AdminPresentationClass(friendlyName = "CountryImpl_baseCountry")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.AUDITABLE_ONLY)
})
public class CountryImpl implements Country, AdminMainEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ABBREVIATION")
    protected String abbreviation;

    @Column(name = "NAME", nullable=false)
    @AdminPresentation(friendlyName = "CountryImpl_Country", order=12, group = "CountryImpl_Address", prominent = true, translatable = true)
    protected String name;

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String Abbreviation) {
        this.abbreviation = Abbreviation;
    }

    public String getName() {
        return DynamicTranslationProvider.getValue(this, "name", name);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!getClass().isAssignableFrom(obj.getClass()))
            return false;
        CountryImpl other = (CountryImpl) obj;
        if (abbreviation == null) {
            if (other.abbreviation != null)
                return false;
        } else if (!abbreviation.equals(other.abbreviation))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((abbreviation == null) ? 0 : abbreviation.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public String getMainEntityName() {
        return getName();
    }
}
