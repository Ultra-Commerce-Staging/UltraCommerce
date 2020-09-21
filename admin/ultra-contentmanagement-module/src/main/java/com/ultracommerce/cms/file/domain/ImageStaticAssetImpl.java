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

import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.openadmin.audit.AdminAuditableListener;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * Created by bpolster.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(value = { AdminAuditableListener.class })
@Table(name = "UC_IMG_STATIC_ASSET")
public class ImageStaticAssetImpl extends StaticAssetImpl implements ImageStaticAsset {

    @Column(name ="WIDTH")
    @AdminPresentation(friendlyName = "ImageStaticAssetImpl_Width",
            order = FieldOrder.LAST + 1000,
            group = GroupName.File_Details,
            readOnly = true)
    protected Integer width;

    @Column(name ="HEIGHT")
    @AdminPresentation(friendlyName = "ImageStaticAssetImpl_Height",
            order = FieldOrder.LAST + 2000,
            group = GroupName.File_Details,
            readOnly = true)
    protected Integer height;

    @Override
    public Integer getWidth() {
        return width;
    }

    @Override
    public void setWidth(Integer width) {
        this.width = width;
    }

    @Override
    public Integer getHeight() {
        return height;
    }

    @Override
    public void setHeight(Integer height) {
        this.height = height;
    }

}
