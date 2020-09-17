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
package com.ultracommerce.cms.structure.domain;

import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.extensibility.jpa.clone.ClonePolicy;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.extensibility.jpa.copy.ProfileEntity;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.PopulateToOneFieldsEnum;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.common.presentation.client.VisibilityEnum;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
@Table(name = "UC_SC_FLD_MAP")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps = true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE)
public class StructuredContentFieldXrefImpl implements StructuredContentFieldXref, Serializable, ProfileEntity {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "StructuredContentFieldId")
    @GenericGenerator(
            name = "StructuredContentFieldId",
            strategy = "com.ultracommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @Parameter(name = "segment_value", value = "StructuredContentFieldXrefImpl"),
                    @Parameter(name = "entity_name", value = "com.ultracommerce.cms.structure.domain.StructuredContentFieldXrefImpl")
            })
    @Column(name = "UC_SC_SC_FIELD_ID")
    protected Long id;

    @ManyToOne(targetEntity = StructuredContentImpl.class, optional = false, cascade = { CascadeType.REFRESH })
    @JoinColumn(name = "SC_ID")
    @AdminPresentation(excluded = true)
    protected StructuredContent structuredContent;

    @ManyToOne(targetEntity = StructuredContentFieldImpl.class, cascade = { CascadeType.ALL })
    @JoinColumn(name = "SC_FLD_ID")
    @ClonePolicy
    @AdminPresentation(fieldType = SupportedFieldType.FOREIGN_KEY)
    protected StructuredContentField structuredContentField;

    @Column(name = "MAP_KEY", nullable = false)
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL)
    protected String key;

    public StructuredContentFieldXrefImpl() {
        //Default constructor for JPA...
    }

    public StructuredContentFieldXrefImpl(StructuredContent sc, StructuredContentField scField, String key) {
        this.structuredContent = sc;
        this.structuredContentField = scField;
        this.key = key;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setStructuredContent(StructuredContent sc) {
        this.structuredContent = sc;
    }

    @Override
    public StructuredContent getStructuredContent() {
        return structuredContent;
    }

    @Override
    public void setStrucuturedContentField(StructuredContentField scField) {
        this.structuredContentField = scField;
    }

    @Override
    public StructuredContentField getStructuredContentField() {
        return structuredContentField;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public <G extends StructuredContentFieldXref> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        StructuredContentFieldXref cloned = createResponse.getClone();
        cloned.setKey(key);
        if (structuredContent != null) {
            cloned.setStructuredContent(structuredContent.createOrRetrieveCopyInstance(context).getClone());
        }
        if (structuredContentField != null) {
            CreateResponse<StructuredContentField> clonedFieldRsp = structuredContentField
                    .createOrRetrieveCopyInstance(context);
            cloned.setStrucuturedContentField(clonedFieldRsp.getClone());
        }
        return createResponse;
    }
}
