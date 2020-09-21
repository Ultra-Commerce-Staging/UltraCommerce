/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.cms.page.service;

import com.ultracommerce.cms.field.domain.FieldDefinition;
import com.ultracommerce.cms.page.domain.Page;
import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.page.dto.PageDTO;


/**
 * @author Andre Azzolini (apazzolini)
 */
public abstract class AbstractPageServiceExtensionHandler extends AbstractExtensionHandler 
        implements PageServiceExtensionHandler {
    
    @Override
    public ExtensionResultStatusType getFieldDefinition(ExtensionResultHolder<FieldDefinition> erh, Page page, 
            String fieldKey) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType overridePageDto(ExtensionResultHolder<PageDTO> erh, PageDTO pageDto, Page page) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}
