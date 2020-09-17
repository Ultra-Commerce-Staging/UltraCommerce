/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.common.web.expression;

import com.ultracommerce.common.module.ModulePresentUtil;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Component;

/**
 * A Thymeleaf Variable Expression intended to provide various information about the modules enabled for this application.
 *
 * @author Elbert Bautista (elbertbautista)
 * @see com.ultracommerce.common.module.ModulePresentUtil
 */
@Component("ucModuleVariableExpression")
@ConditionalOnTemplating
public class ModuleVariableExpression implements UltraVariableExpression {

    @Override
    public String getName() {
        return "module";
    }

    public boolean isPresent (String moduleInQuestion) {
        return ModulePresentUtil.isPresent(moduleInQuestion);
    }

}
