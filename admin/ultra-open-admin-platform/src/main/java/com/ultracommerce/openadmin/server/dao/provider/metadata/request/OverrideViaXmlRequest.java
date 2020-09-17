/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.server.dao.provider.metadata.request;

import com.ultracommerce.openadmin.server.dao.DynamicEntityDao;

/**
 * Contains the requested config key, ceiling entity, metadata and support classes.
 *
 * @author Jeff Fischer
 */
public class OverrideViaXmlRequest {

    private final String requestedConfigKey;
    private final String requestedCeilingEntity;
    private final String prefix;
    private final Boolean parentExcluded;
    private final DynamicEntityDao dynamicEntityDao;

    public OverrideViaXmlRequest(String requestedConfigKey, String requestedCeilingEntity, String prefix, Boolean parentExcluded, DynamicEntityDao dynamicEntityDao) {
        this.requestedConfigKey = requestedConfigKey;
        this.requestedCeilingEntity = requestedCeilingEntity;
        this.prefix = prefix;
        this.parentExcluded = parentExcluded;
        this.dynamicEntityDao = dynamicEntityDao;
    }

    public String getRequestedConfigKey() {
        return requestedConfigKey;
    }

    public String getRequestedCeilingEntity() {
        return requestedCeilingEntity;
    }

    public String getPrefix() {
        return prefix;
    }

    public Boolean getParentExcluded() {
        return parentExcluded;
    }

    public DynamicEntityDao getDynamicEntityDao() {
        return dynamicEntityDao;
    }
}
