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

import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.web.deeplink.DeepLink;
import com.ultracommerce.presentation.model.UltraTemplateContext;

import java.util.List;
import java.util.Map;

/**
 * Abstract implementation of {@link ContentProcessorExtensionHandler}
 * 
 * @author Andre Azzolini (apazzolini)
 */
public abstract class AbstractContentProcessorExtensionHandler extends AbstractExtensionHandler implements ContentProcessorExtensionHandler {

    @Override
    public ExtensionResultStatusType addAdditionalFieldsToModel(String tagName, Map<String, String> tagAttributes, Map<String, Object> newModelVars, UltraTemplateContext context) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType addExtensionFieldDeepLink(List<DeepLink> links, String tagName, Map<String, String> tagAttributes, UltraTemplateContext context) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType postProcessDeepLinks(List<DeepLink> links) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}
