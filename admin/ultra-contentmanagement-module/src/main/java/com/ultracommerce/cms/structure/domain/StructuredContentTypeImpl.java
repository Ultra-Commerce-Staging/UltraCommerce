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

import com.ultracommerce.common.admin.domain.AdminMainEntity;
import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.extensibility.jpa.copy.ProfileEntity;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.PopulateToOneFieldsEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
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
 * Created by bpolster.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_SC_TYPE")
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="ucCMSElements")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "StructuredContentTypeImpl_baseStructuredContentType")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class StructuredContentTypeImpl implements StructuredContentType, AdminMainEntity, ProfileEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "StructuredContentTypeId")
    @GenericGenerator(
        name="StructuredContentTypeId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="StructuredContentTypeImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.cms.structure.domain.StructuredContentTypeImpl")
        }
    )
    @Column(name = "SC_TYPE_ID")
    protected Long id;

    @Column (name = "NAME")
    @AdminPresentation(friendlyName = "StructuredContentTypeImpl_Name", order = 1, gridOrder = 1, group = "StructuredContentTypeImpl_Details", prominent = true)
    @Index(name="SC_TYPE_NAME_INDEX", columnNames={"NAME"})
    protected String name;

    @Column (name = "DESCRIPTION")
    protected String description;

    @ManyToOne(targetEntity = StructuredContentFieldTemplateImpl.class)
    @JoinColumn(name="SC_FLD_TMPLT_ID")
    protected StructuredContentFieldTemplate structuredContentFieldTemplate;

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

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public StructuredContentFieldTemplate getStructuredContentFieldTemplate() {
        return structuredContentFieldTemplate;
    }

    @Override
    public void setStructuredContentFieldTemplate(StructuredContentFieldTemplate scft) {
        this.structuredContentFieldTemplate = scft;
    }

    @Override
    public String getMainEntityName() {
        return getName();
    }

    @Override
    public <G extends StructuredContentType> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        StructuredContentType cloned = createResponse.getClone();
        cloned.setDescription(description);
        cloned.setName(name);
        if (structuredContentFieldTemplate != null) {
            CreateResponse<StructuredContentFieldTemplate> clonedTemplate = structuredContentFieldTemplate
                    .createOrRetrieveCopyInstance(context);
            cloned.setStructuredContentFieldTemplate(clonedTemplate.getClone());
        }
        return createResponse;
    }
}

