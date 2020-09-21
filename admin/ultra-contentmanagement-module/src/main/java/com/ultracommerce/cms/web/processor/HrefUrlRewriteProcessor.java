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

package com.ultracommerce.cms.web.processor;

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.file.service.StaticAssetPathService;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.model.UltraAttributeModifier;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Similar to {@link UrlRewriteProcessor} but handles href tags.   
 * Mainly those that have a useCdn=true attribute or those that are inside a script tag.
 * 
 * @author bpolster
 */
@Component("ucHrefUrlRewriteProcessor")
@ConditionalOnTemplating
public class HrefUrlRewriteProcessor extends UrlRewriteProcessor {

    @Resource(name = "ucStaticAssetPathService")
    protected StaticAssetPathService staticAssetPathService;

    private static final String LINK = "link";
    private static final String HREF = "href";

    @Override
    public String getName() {
        return HREF;
    }

    @Override
    public UltraAttributeModifier getModifiedAttributes(String tagName, Map<String, String> tagAttributes, String attributeName,
            String attributeValue, UltraTemplateContext context) {
        String useCDN = tagAttributes.get("useCDN");
        String hrefValue = attributeValue;
        if (LINK.equals(tagName) || StringUtils.equals("true", useCDN)) {
            hrefValue = super.getFullAssetPath(tagName, attributeValue, context);
        } else {
            hrefValue = super.parsePath(attributeValue, context);
        }
        Map<String, String> newAttributes = new HashMap<>();
        newAttributes.put(HREF, hrefValue);
        return new UltraAttributeModifier(newAttributes);
    }

}
