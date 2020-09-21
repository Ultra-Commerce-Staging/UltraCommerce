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
package com.ultracommerce.cms.web.deeplink;

import com.ultracommerce.cms.structure.domain.StructuredContent;
import com.ultracommerce.common.structure.dto.StructuredContentDTO;
import com.ultracommerce.common.web.deeplink.DeepLink;
import com.ultracommerce.common.web.deeplink.DeepLinkService;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides deep links for {@link StructuredContent} items.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class ContentDeepLinkServiceImpl extends DeepLinkService<StructuredContentDTO> {

    protected String structuredContentAdminPath;

    @Override
    protected List<DeepLink> getLinksInternal(StructuredContentDTO item) {
        List<DeepLink> links = new ArrayList<DeepLink>();

        links.add(new DeepLink()
            .withAdminBaseUrl(getAdminBaseUrl())
            .withUrlFragment(structuredContentAdminPath + item.getId())
            .withDisplayText("Edit")
            .withSourceObject(item));

        return links;
    }

    public String getStructuredContentAdminPath() {
        return structuredContentAdminPath;
    }

    public void setStructuredContentAdminPath(String structuredContentAdminPath) {
        this.structuredContentAdminPath = structuredContentAdminPath;
    }

}
