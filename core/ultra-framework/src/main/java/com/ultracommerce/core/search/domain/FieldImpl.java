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
package com.ultracommerce.core.search.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.ultracommerce.common.admin.domain.AdminMainEntity;
import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.i18n.service.DynamicTranslationProvider;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.RequiredOverride;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.common.presentation.client.VisibilityEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_FIELD")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ucStandardElements")
@DirectCopyTransform({
    @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE),
    @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.AUDITABLE_ONLY),
    @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTI_PHASE_ADD)

})
public class FieldImpl implements Field, FieldAdminPresentation, AdminMainEntity {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2915813511754425605L;

    @Id
    @GeneratedValue(generator = "FieldId")
    @GenericGenerator(
        name="FieldId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FieldImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.core.search.domain.FieldImpl")
        }
    )
    @Column(name = "FIELD_ID")
    @AdminPresentation(friendlyName = "FieldImpl_ID", group = "FieldImpl_general",visibility=VisibilityEnum.HIDDEN_ALL)
    protected Long id;
    
    // This is a ultra enumeration
    @Column(name = "ENTITY_TYPE", nullable = false)
    @Index(name="ENTITY_TYPE_INDEX", columnNames={"ENTITY_TYPE"})
    @AdminPresentation(friendlyName = "FieldImpl_EntityType",
            group = GroupName.General, order = FieldOrder.ENTITY_TYPE,
            prominent = true, gridOrder = 1,
            fieldType = SupportedFieldType.ULTRA_ENUMERATION,
            ultraEnumeration = "com.ultracommerce.core.search.domain.FieldEntity",
            defaultValue = "com.ultracommerce.core.catalog.domain.ProductImpl",
            requiredOverride = RequiredOverride.REQUIRED)
    protected String entityType;
    
    @Column(name = "FRIENDLY_NAME")
    @AdminPresentation(friendlyName = "FieldImpl_friendlyName",
            group = GroupName.General, order = FieldOrder.FRIENDLY_NAME,
            prominent = true, gridOrder = 2, translatable = true,
            requiredOverride = RequiredOverride.REQUIRED)
    protected String friendlyName;

    @Column(name = "PROPERTY_NAME", nullable = false)
    @AdminPresentation(friendlyName = "FieldImpl_propertyName",
            group = GroupName.General, order = FieldOrder.PROPERTY_NAME,
            prominent = true, gridOrder = 3,
            requiredOverride = RequiredOverride.REQUIRED)
    protected String propertyName;

    @Column(name = "OVERRIDE_GENERATED_PROP_NAME")
    @AdminPresentation(friendlyName = "FieldImpl_overrideGeneratedPropertyName",
            group = GroupName.General, order = FieldOrder.OVERRIDE_GENERATED_PROPERTY_NAME)
    protected Boolean overrideGeneratedPropertyName = false;

    @Column(name = "ABBREVIATION")
    @AdminPresentation(friendlyName = "FieldImpl_abbreviation",
            group = GroupName.General, order = FieldOrder.ABBREVIATION,
            excluded = true)
    protected String abbreviation;

    @Column(name = "TRANSLATABLE")
    @AdminPresentation(friendlyName = "FieldImpl_translatable",
            group = GroupName.General, order = FieldOrder.TRANSLATABLE,
            excluded = true)
    protected Boolean translatable = false;

    @Override
    public String getQualifiedFieldName() {
        return getEntityType().getFriendlyType() + "." + propertyName;
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
    public FieldEntity getEntityType() {
        return FieldEntity.getInstance(entityType);
    }

    @Override
    public void setEntityType(FieldEntity entityType) {
        this.entityType = entityType.getType();
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public Boolean getOverrideGeneratedPropertyName() {
        return overrideGeneratedPropertyName  == null ? false : overrideGeneratedPropertyName;
    }

    @Override
    public void setOverrideGeneratedPropertyName(Boolean overrideGeneratedPropertyName) {
        this.overrideGeneratedPropertyName = overrideGeneratedPropertyName == null ? false : overrideGeneratedPropertyName;
    }

    @Override
    public String getAbbreviation() {
        return abbreviation;
    }

    @Override
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String getFriendlyName() {
        return DynamicTranslationProvider.getValue(this, "friendlyName", friendlyName);
    }

    @Override
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public Boolean getTranslatable() {
        return translatable == null ? false : translatable;
    }

    @Override
    public void setTranslatable(Boolean translatable) {
        this.translatable = translatable;
    }

    @Deprecated
    @Override
    public List<SearchConfig> getSearchConfigs() {
        throw new UnsupportedOperationException("The default Field implementation does not support search configs");
    }

    @Deprecated
    @Override
    public void setSearchConfigs(List<SearchConfig> searchConfigs) {
        throw new UnsupportedOperationException("The default Field implementation does not support search configs");
    }

    @Override
    public String getMainEntityName() {
        return getFriendlyName();
    }

    @Override
    public <G extends Field> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws
            CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        Field cloned = createResponse.getClone();
        cloned.setAbbreviation(abbreviation);
        cloned.setFriendlyName(friendlyName);
        cloned.setPropertyName(propertyName);
        cloned.setTranslatable(translatable);
        cloned.setEntityType(getEntityType());
        return createResponse;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        Field other = (Field) obj;

        return getEntityType().getType().equals(other.getEntityType().getType()) && getPropertyName().equals(other.getPropertyName());

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(entityType)
                .append(friendlyName)
                .append(propertyName)
                .append(overrideGeneratedPropertyName)
                .append(abbreviation)
                .append(translatable)
                .toHashCode();
    }
}
