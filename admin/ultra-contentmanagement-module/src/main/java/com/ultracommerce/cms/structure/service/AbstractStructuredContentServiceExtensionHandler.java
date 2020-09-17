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
package com.ultracommerce.cms.structure.service;

import com.ultracommerce.cms.structure.domain.StructuredContent;
import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.structure.dto.StructuredContentDTO;

import java.util.List;

/**
 * Extension handler for the {@link StructuredContentService}
 *
 * @author bpolster
 */
public class AbstractStructuredContentServiceExtensionHandler extends AbstractExtensionHandler
        implements StructuredContentServiceExtensionHandler {

    public ExtensionResultStatusType populateAdditionalStructuredContentFields(StructuredContent sc,
            StructuredContentDTO dto, boolean secure) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }


    public ExtensionResultStatusType modifyStructuredContentDtoList(List<StructuredContentDTO> structuredContentList,
            ExtensionResultHolder resultHolder) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}
