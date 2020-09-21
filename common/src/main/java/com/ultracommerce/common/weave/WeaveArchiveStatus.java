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
package com.ultracommerce.common.weave;

import com.ultracommerce.common.persistence.ArchiveStatus;
import com.ultracommerce.common.persistence.Status;
import com.ultracommerce.common.presentation.AdminPresentation;

import javax.persistence.Embedded;

/**
 * @author by reginaldccole
 */
public final class WeaveArchiveStatus implements Status {

    @Embedded
    protected ArchiveStatus archiveStatus;


    @Override
    public void setArchived(Character archived) {
            getEmbeddableArchiveStatus(true).setArchived(archived);
    }

    private ArchiveStatus getEmbeddableArchiveStatus(boolean assign) {
        ArchiveStatus temp = archiveStatus;
        if (temp == null) {
            temp = new ArchiveStatus();
            if (assign) {
                archiveStatus = temp;
            }
        }
        return temp;
    }

    @Override
    public Character getArchived() {
        return getEmbeddableArchiveStatus(false).getArchived();
    }

    @Override
    public boolean isActive() {
         return 'Y'!=getArchived();
    }
}
