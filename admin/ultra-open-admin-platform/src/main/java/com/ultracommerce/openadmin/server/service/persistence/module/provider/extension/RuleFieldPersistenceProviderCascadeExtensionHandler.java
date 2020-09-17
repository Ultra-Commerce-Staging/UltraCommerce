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
package com.ultracommerce.openadmin.server.service.persistence.module.provider.extension;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.rule.QuantityBasedRule;
import com.ultracommerce.openadmin.web.rulebuilder.dto.DataDTO;

/**
 * Special case - Handle propagated state for rules. This only applies in the presence of the enterprise module.
 *
 * @author Jeff Fischer
 */
public interface RuleFieldPersistenceProviderCascadeExtensionHandler extends ExtensionHandler {

    /**
     * Setup proper prod record enterprise state for a propagated rule addition.
     *
     * @param rule
     * @param dataDTO
     * @param resultHolder
     * @return
     */
    ExtensionResultStatusType postCascadeAdd(Object rule, DataDTO dataDTO, ExtensionResultHolder resultHolder);

}
