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
package com.ultracommerce.common.web;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.classloader.release.ThreadLocalManager;
import com.ultracommerce.common.site.domain.Theme;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

/**
 * @author Stanislav Fedorov
 * @see {@link UltraThemeResolverFilter}
 */
@Component("ucThemeProcessor")
public class UltraThemeProcessor extends AbstractUltraWebRequestProcessor {

    protected final Log LOG = LogFactory.getLog(getClass());

    @Resource(name = "ucThemeResolver")
    protected UltraThemeResolver themeResolver;

    @Override
    public void process(WebRequest request) {

        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        Theme originalTheme = brc.getTheme();

        // Note that this must happen after the request context is set up as resolving a theme is dependent on site
        Theme newTheme = themeResolver.resolveTheme(request);

        //Track if the theme changed
        if (originalTheme != null && newTheme != null && ObjectUtils.compare(originalTheme.getId(), newTheme.getId()) != 0) {
            Map<String, Object> properties = brc.getAdditionalProperties();
            properties.put(UltraThemeResolver.BRC_THEME_CHANGE_STATUS, true);
        }

        brc.setTheme(newTheme);

    }

    @Override
    public void postProcess(WebRequest request) {
        ThreadLocalManager.remove();
    }

}
