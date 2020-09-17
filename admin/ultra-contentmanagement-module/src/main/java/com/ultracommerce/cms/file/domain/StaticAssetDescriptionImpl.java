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
package com.ultracommerce.cms.file.domain;

import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.client.VisibilityEnum;
import com.ultracommerce.openadmin.audit.AdminAuditableListener;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * Created by bpolster.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_STATIC_ASSET_DESC")
@EntityListeners(value = { AdminAuditableListener.class })
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="ucCMSElements")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.AUDITABLE_ONLY)
})
public class StaticAssetDescriptionImpl implements StaticAssetDescription {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "StaticAssetDescriptionId")
    @GenericGenerator(
        name="StaticAssetDescriptionId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="StaticAssetDescriptionImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.cms.file.domain.StaticAssetDescriptionImpl")
        }
    )
    @Column(name = "STATIC_ASSET_DESC_ID")
    protected Long id;

    @Column (name = "DESCRIPTION")
    @AdminPresentation(friendlyName = "StaticAssetDescriptionImpl_Description", prominent = true)
    protected String description;

    @Column (name = "LONG_DESCRIPTION")
    @AdminPresentation(friendlyName = "StaticAssetDescriptionImpl_Long_Description", largeEntry = true, visibility = VisibilityEnum.GRID_HIDDEN)
    protected String longDescription;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
    public String getLongDescription() {
        return longDescription;
    }

    @Override
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    @Override
    public StaticAssetDescription cloneEntity() {
        StaticAssetDescriptionImpl newAssetDescription = new StaticAssetDescriptionImpl();
        newAssetDescription.description = description;
        newAssetDescription.longDescription = longDescription;

        return newAssetDescription;
    }

    @Override
    public <G extends StaticAssetDescription> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        StaticAssetDescription cloned = createResponse.getClone();
        cloned.setDescription(description);
        cloned.setLongDescription(longDescription);
        return createResponse;
    }
}

