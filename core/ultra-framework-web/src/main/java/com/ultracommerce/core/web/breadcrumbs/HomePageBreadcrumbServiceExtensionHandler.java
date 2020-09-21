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
package com.ultracommerce.core.web.breadcrumbs;

import com.ultracommerce.common.breadcrumbs.dto.BreadcrumbDTO;
import com.ultracommerce.common.breadcrumbs.dto.BreadcrumbDTOType;
import com.ultracommerce.common.breadcrumbs.service.BreadcrumbHandlerDefaultPriorities;
import com.ultracommerce.common.breadcrumbs.service.BreadcrumbServiceExtensionManager;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


/**
 * Contributes a breadcrumb (typically the first breadcrumb).    Simply the word home
 * as defined by the property "breadcrumb.homepageText" and the url "/".
 *  
 * @author bpolster
 *
 */
@Service("ucHomePageBreadcrumbServiceExtensionHandler")
public class HomePageBreadcrumbServiceExtensionHandler extends AbstractBreadcrumbServiceExtensionHandler {

    @Value("${breadcrumb.homepageText:Home}")
    protected String homePageText;

    @Resource(name = "ucBreadcrumbServiceExtensionManager")
    protected BreadcrumbServiceExtensionManager extensionManager;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    @Override
    public ExtensionResultStatusType modifyBreadcrumbList(String url, Map<String, String[]> params,
            ExtensionResultHolder<List<BreadcrumbDTO>> holder) {
        BreadcrumbDTO homePageDto = new BreadcrumbDTO();
        homePageDto.setText(homePageText);
        homePageDto.setLink("/");
        homePageDto.setType(BreadcrumbDTOType.HOME);
        holder.getResult().add(0, homePageDto);
        return ExtensionResultStatusType.HANDLED_CONTINUE;
    }

    @Override
    public int getDefaultPriority() {
        return BreadcrumbHandlerDefaultPriorities.HOME_CRUMB;
    }

}
