/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.web.expression;

import com.ultracommerce.common.web.expression.UltraVariableExpression;
import com.ultracommerce.openadmin.web.form.entity.EntityForm;
import com.ultracommerce.openadmin.web.form.entity.Tab;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Component;

/**
 * A {@link UltraVariableExpression} that assists with operations for Thymeleaf-layer operations on entity forms.
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Component("ucEntityFormVariableExpression")
@ConditionalOnTemplating
public class EntityFormVariableExpression implements UltraVariableExpression {
    
    @Override
    public String getName() {
        return "ef";
    }
    
    public boolean isTabActive(EntityForm ef, Tab tab) {
        boolean foundVisibleTab = false;

        for (Tab t : ef.getTabs()) {
            if (tab == t && !foundVisibleTab) {
                return true;
            } else if (tab != t && t.getIsVisible()) {
                foundVisibleTab = true;
            }
        }

        return false;
    }

}
