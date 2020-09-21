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

import com.ultracommerce.cms.structure.domain.StructuredContentFieldGroupXref;
import com.ultracommerce.cms.structure.domain.StructuredContentFieldGroupXrefImpl;
import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.extensibility.jpa.clone.ClonePolicyArchive;
import com.ultracommerce.common.extensibility.jpa.clone.ClonePolicyCollectionOverride;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.extensibility.jpa.copy.ProfileEntity;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Created by bpolster.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_FLD_GROUP")
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="ucCMSElements")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps = true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class FieldGroupImpl implements FieldGroup, ProfileEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "FieldGroupId")
    @GenericGenerator(
        name="FieldGroupId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FieldGroupImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.cms.field.domain.FieldGroupImpl")
        }
    )
    @Column(name = "FLD_GROUP_ID")
    protected Long id;

    @Column (name = "NAME")
    protected String name;

    @Column (name = "INIT_COLLAPSED_FLAG")
    protected Boolean initCollapsedFlag = false;

    @OneToMany(mappedBy = "fieldGroup", targetEntity = FieldDefinitionImpl.class, cascade = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucCMSElements")
    @OrderBy("fieldOrder")
    @BatchSize(size = 20)
    @ClonePolicyCollectionOverride
    @ClonePolicyArchive
    protected List<FieldDefinition> fieldDefinitions = new ArrayList<FieldDefinition>();

    @Column (name = "IS_MASTER_FIELD_GROUP")
    protected Boolean isMasterFieldGroup = false;

    @OneToMany(targetEntity = StructuredContentFieldGroupXrefImpl.class, mappedBy = "fieldGroup", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucCMSElements")
    @OrderBy("groupOrder")
    @BatchSize(size = 20)
    protected List<StructuredContentFieldGroupXref> fieldGroupXrefs = new ArrayList<StructuredContentFieldGroupXref>();

    @Override
    public List<StructuredContentFieldGroupXref> getFieldGroupXrefs() {
        return fieldGroupXrefs;
    }

    @Override
    public void setFieldGroupXrefs(List<StructuredContentFieldGroupXref> fieldGroupXrefs) {
        this.fieldGroupXrefs = fieldGroupXrefs;
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

    @Override
    public Boolean getInitCollapsedFlag() {
        return initCollapsedFlag;
    }

    @Override
    public void setInitCollapsedFlag(Boolean initCollapsedFlag) {
        this.initCollapsedFlag = initCollapsedFlag;
    }

    @Override
    public List<FieldDefinition> getFieldDefinitions() {
        return fieldDefinitions;
    }

    @Override
    public void setFieldDefinitions(List<FieldDefinition> fieldDefinitions) {
        this.fieldDefinitions = fieldDefinitions;
    }

    @Override
    public <G extends FieldGroup> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context)
            throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }

        FieldGroup cloned = createResponse.getClone();
        cloned.setName(name);
        cloned.setIsMasterFieldGroup(isMasterFieldGroup);
        cloned.setInitCollapsedFlag(initCollapsedFlag);

        for (FieldDefinition fieldDefinition : fieldDefinitions) {
            FieldDefinition clonedDef = fieldDefinition.createOrRetrieveCopyInstance(context).getClone();
            cloned.getFieldDefinitions().add(clonedDef);
        }
        for (StructuredContentFieldGroupXref entry : fieldGroupXrefs) {
            CreateResponse<StructuredContentFieldGroupXref>  clonedDef = entry.createOrRetrieveCopyInstance(context);
            clonedDef.getClone().setFieldGroup(cloned);
            cloned.getFieldGroupXrefs().add(clonedDef.getClone());
        }
        return createResponse;
    }

    @Override
    public Boolean isMasterFieldGroup() {
        return isMasterFieldGroup != null ? isMasterFieldGroup : Boolean.FALSE;
    }

    @Override
    public void setIsMasterFieldGroup(Boolean isMasterFieldGroup) {
        this.isMasterFieldGroup = isMasterFieldGroup;
    }
}
