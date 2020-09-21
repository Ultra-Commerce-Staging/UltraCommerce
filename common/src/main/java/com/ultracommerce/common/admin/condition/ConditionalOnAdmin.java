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
package com.ultracommerce.common.admin.condition;

import com.ultracommerce.common.config.AdminOnlyTarget;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this bean should not be created unless the ultra-open-admin-platform exists on the classpath. This
 * can be used for beans that are designed to only exist in the admin (like custom persistence handlers, admin
 * controllers, etc)
 *
 * @author Philip Baggett (pbaggett)
 * @author Brandon Hines (bhines)
 * @see AdminExistsCondition
 * @since 5.2
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnBean(AdminOnlyTarget.class)
public @interface ConditionalOnAdmin {
}
