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
package com.ultracommerce.common.persistence;

import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.client.VisibilityEnum;
import com.ultracommerce.common.sandbox.SandBoxNonProductionSkip;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Jeff Fischer
 */
@Embeddable
public class ArchiveStatus implements Serializable, SandBoxNonProductionSkip {

    private static final long serialVersionUID = 1L;

    @Column(name = "ARCHIVED")
    @AdminPresentation(friendlyName = "archived", visibility = VisibilityEnum.HIDDEN_ALL, group = "ArchiveStatus")
    protected Character archived = 'N';

    public Character getArchived() {
        return archived;
    }

    public void setArchived(Character archived) {
        this.archived = archived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!getClass().isAssignableFrom(o.getClass())) return false;

        ArchiveStatus that = (ArchiveStatus) o;

        if (archived != null ? !archived.equals(that.archived) : that.archived != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return archived != null ? archived.hashCode() : 0;
    }
}
