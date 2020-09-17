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
package com.ultracommerce.common.web.expression;

/**
 * Classes that implement this interface will be exposed to the Thymeleaf expression evaluation context.
 * If an implementing class defines its name as "theme" and has a method called attr(String name), that method
 * could then be invoked by ${#theme.attr('someName')}.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public interface UltraVariableExpression {
    
    public String getName();
    
}
