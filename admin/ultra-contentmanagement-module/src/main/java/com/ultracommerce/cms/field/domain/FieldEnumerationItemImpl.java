/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.field.domain;

import com.ultracommerce.common.enumeration.domain.DataDrivenEnumerationValueImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by jfischer
 * @deprecated use {@link DataDrivenEnumerationValueImpl} instead
 */
@Deprecated
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_FLD_ENUM_ITEM")
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="ucCMSElements")
public class FieldEnumerationItemImpl implements FieldEnumerationItem {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "FieldEnumerationItemId")
    @GenericGenerator(
        name="FieldEnumerationItemId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FieldEnumerationItemImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.cms.field.domain.FieldEnumerationItemImpl")
        }
    )
    @Column(name = "FLD_ENUM_ITEM_ID")
    protected Long id;

    @Column (name = "NAME")
    protected String name;

    @Column (name = "FRIENDLY_NAME")
    protected String friendlyName;

    @Column(name="FLD_ORDER")
    protected int fieldOrder;

    @ManyToOne(targetEntity = FieldEnumerationImpl.class)
    @JoinColumn(name = "FLD_ENUM_ID")
    protected FieldEnumeration fieldEnumeration;

    @Override
    public FieldEnumeration getFieldEnumeration() {
        return fieldEnumeration;
    }

    @Override
    public void setFieldEnumeration(FieldEnumeration fieldEnumeration) {
        this.fieldEnumeration = fieldEnumeration;
    }

    @Override
    public int getFieldOrder() {
        return fieldOrder;
    }

    @Override
    public void setFieldOrder(int fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    @Override
    public String getFriendlyName() {
        return friendlyName;
    }

    @Override
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
