/*
 * #%L
 * ultra-theme
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
package com.ultracommerce.common.web.resource.resolver;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.site.domain.Site;
import com.ultracommerce.common.web.UltraRequestContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.resource.AbstractResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * A {@link ResourceResolver} that replaces the //UC-SERVLET-CONTEXT and //UC-SITE-BASEURL" 
 * tokens before serving the UC.js file.
 * 
 * This component modifies the path and works in conjunction with the {@link UCJSResourceResolver}
 * which loads the modified file.
 * 
 * The processes were split to allow for caching of the resource but not the URL path.
 * 
 * @since 4.0
 * 
 * @author Reggie Cole
 * @author Brian Polster
 * @since Ultra 4.0
 */
@Component("ucUCJSUrlPathResolver")
public class UCJSUrlPathResolver extends AbstractResourceResolver implements Ordered {

    protected static final Log LOG = LogFactory.getLog(UCJSUrlPathResolver.class);

    private static final String UC_JS_NAME = "UC.js";

    private int order = UltraResourceResolverOrder.UC_JS_PATH_RESOLVER;

    @Override
    protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations,
            ResourceResolverChain chain) {
        if (resourceUrlPath.contains(UC_JS_NAME)) {
            Site site = UltraRequestContext.getUltraRequestContext().getNonPersistentSite();
            if (site != null && site.getId() != null) {
                return addVersion(resourceUrlPath, "-"+site.getId());
            } else {
                return resourceUrlPath;
            }                       
        }
        return chain.resolveUrlPath(resourceUrlPath, locations);
    }
    
    @Override
    protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath,
            List<? extends Resource> locations, ResourceResolverChain chain) {
        return chain.resolveResource(request, requestPath, locations);
    }

    protected String addVersion(String requestPath, String version) {
        String baseFilename = StringUtils.stripFilenameExtension(requestPath);
        String extension = StringUtils.getFilenameExtension(requestPath);
        return baseFilename + version + "." + extension;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
