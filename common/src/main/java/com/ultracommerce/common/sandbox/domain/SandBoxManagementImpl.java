/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.common.sandbox.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.admin.domain.AdminMainEntity;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.PopulateToOneFieldsEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * This class is required mostly as a workaround for an issue in Hibernate. It's obscure, but I'll try to explain.
 * SandBox ids are used as discriminators in workflow. SandBoxes themselves are also able to be managed in the
 * admin (add new sandbox, etc...) Site ids are used as discriminators in multitenant. When workflow and multitenant
 * are used together, both discriminators are in effect. Because sandboxes can be managed in the admin, it is required
 * that they have a site discriminator to be managed in the multitenant admin. This intermingling of references
 * ends up causing this exception at runtime during, for example, a product save:
 *
 * HibernateException: Found two representations of same collection
 *
 * To workaround, we use this management entity that exposes the properties seamlessly of SandBox to the admin, but
 * holds the site discriminator on its own table (rather than UC_SANDBOX), which fixes the issue.
 *
 * @author Jeff Fischer
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="UC_SANDBOX_MGMT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucSandBoxElements")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE)
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.AUDITABLE_ONLY),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTI_PHASE_ADD)
})
public class SandBoxManagementImpl implements AdminMainEntity, SandBoxManagement {

    private static final Log LOG = LogFactory.getLog(SandBoxManagementImpl.class);
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "SandBoxMgmtId")
    @GenericGenerator(
        name="SandBoxMgmtId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="SandBoxManagementImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.common.sandbox.domain.SandBoxManagementImpl")
        }
    )
    @Column(name = "SANDBOX_MGMT_ID")
    protected Long id;

    @OneToOne(targetEntity = SandBoxImpl.class, cascade={CascadeType.ALL}, optional = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucSandBoxElements")
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
    @JoinColumn(name = "SANDBOX_ID")
    protected SandBox sandBox;

    @Override
    public String getMainEntityName() {
        return sandBox.getName();
    }

    @Override
    public SandBox getSandBox() {
        return sandBox;
    }

    @Override
    public void setSandBox(SandBox sandBox) {
        this.sandBox = sandBox;
    }
}
