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
package com.ultracommerce.common.jmx;

import org.apache.commons.lang.StringUtils;
import org.springframework.jmx.export.metadata.InvalidMetadataException;
import org.springframework.jmx.export.metadata.ManagedResource;

/**
 * 
 * @author jfischer
 *
 */
public class AnnotationJmxAttributeSource extends org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource {
    
    private final String appName;
    
    public AnnotationJmxAttributeSource(String appName) {
        this.appName = appName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ManagedResource getManagedResource(Class beanClass) throws InvalidMetadataException {
        ManagedResource resource = super.getManagedResource(beanClass);
        if (resource != null && appName != null) {
            String objectName = resource.getObjectName();
            if(StringUtils.isNotBlank(objectName)) {
                objectName += "." + appName;
            }
            resource.setObjectName(objectName);
        }
        return resource;
    }
    
}
