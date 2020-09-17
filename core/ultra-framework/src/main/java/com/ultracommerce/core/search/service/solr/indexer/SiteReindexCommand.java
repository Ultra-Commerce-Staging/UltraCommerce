/*
 * #%L
 * UltraCommerce Framework
 * %%
 * Copyright (C) 2009 - 2019 Ultra Commerce
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
package com.ultracommerce.core.search.service.solr.indexer;

import org.springframework.util.Assert;

public class SiteReindexCommand extends SolrUpdateCommand {

    private static final long serialVersionUID = 1L;

    private final Long siteId;
    
    public SiteReindexCommand(Long siteId) {
        Assert.notNull(siteId, "siteId cannot be null.");
        this.siteId = siteId;
    }
    
    public Long getSiteId() {
        return siteId;
    }

    @Override
    public int hashCode() {
        return getSiteId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof SiteReindexCommand) {
            if (getSiteId().equals(((SiteReindexCommand) obj).getSiteId())) {
                return true;
            }
        }
        return false;
    }
    
    
}
