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
package com.ultracommerce.common.web.filter;

import com.ultracommerce.common.i18n.service.TranslationConsiderationContext;
import com.ultracommerce.common.i18n.service.TranslationService;
import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.common.web.AbstractUltraWebRequestProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;

/**
 * This processor is responsible for setting up the translation context.   It is intended to be used
 * by both typical Web applications and called from a ServletFilter (such as "TranslationFilter") or 
 * from a portletFilter (such as "TranslationInterceptor")
 * 
 * @author bpolster
 */
@Component("ucTranslationRequestProcessor")
public class TranslationRequestProcessor extends AbstractUltraWebRequestProcessor {
    
    @Resource(name = "ucTranslationService")
    protected TranslationService translationService;
    
    protected boolean getTranslationEnabled() {
        return UCSystemProperty.resolveBooleanSystemProperty("i18n.translation.enabled");
    }

    @Override
    public void process(WebRequest request) {
        TranslationConsiderationContext.setTranslationConsiderationContext(getTranslationEnabled());
        TranslationConsiderationContext.setTranslationService(translationService);
    }
}
