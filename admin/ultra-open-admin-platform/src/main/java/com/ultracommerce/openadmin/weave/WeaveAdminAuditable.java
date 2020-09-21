/*
 * #%L
 * ultra-enterprise
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
package com.ultracommerce.openadmin.weave;

import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.client.VisibilityEnum;
import com.ultracommerce.openadmin.audit.AdminAudit;
import com.ultracommerce.openadmin.audit.AdminAuditable;
import com.ultracommerce.openadmin.audit.AdminAuditableListener;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.EntityListeners;

/**
 * @author by ckittrell
 */
@EntityListeners(value = { AdminAuditableListener.class })
public final class WeaveAdminAuditable implements AdminAudit {

    @Embedded
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL)
    protected AdminAuditable auditable = new AdminAuditable();

    @Override
    public Long getCreatedBy() {
        return getEmbeddableAdminAuditable(false).getCreatedBy();
    }

    @Override
    public Date getDateCreated() {
        return getEmbeddableAdminAuditable(false).getDateCreated();
    }

    @Override
    public Date getDateUpdated() {
        return getEmbeddableAdminAuditable(false).getDateUpdated();
    }

    @Override
    public void setDateCreated(Date dateCreated) {
        getEmbeddableAdminAuditable(true).setDateCreated(dateCreated);
    }

    @Override
    public void setDateUpdated(Date dateUpdated) {
        getEmbeddableAdminAuditable(true).setDateUpdated(dateUpdated);
    }

    @Override
    public void setCreatedBy(Long createdBy) {
        getEmbeddableAdminAuditable(true).setCreatedBy(createdBy);
    }

    @Override
    public Long getUpdatedBy() {
        return getEmbeddableAdminAuditable(false).getUpdatedBy();
    }

    @Override
    public void setUpdatedBy(Long updatedBy) {
        getEmbeddableAdminAuditable(true).setUpdatedBy(updatedBy);
    }

    public AdminAuditable getEmbeddableAdminAuditable(boolean assign) {
        AdminAuditable temp = auditable;
        if (temp == null) {
            temp = new AdminAuditable();
            if (assign) {
                auditable = temp;
            }
        }
        return temp;
    }
}
