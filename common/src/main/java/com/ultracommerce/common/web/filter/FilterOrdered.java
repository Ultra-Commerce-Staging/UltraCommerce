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
package com.ultracommerce.common.web.filter;

/**
 * For Spring Boot implementations, use the {@link org.springframework.core.Ordered} interface, in conjunction with these
 * order values, on any {@link javax.servlet.Filter} implementation beans instantiated through Spring. This serves to setup
 * proper overall http filtering ordering in relation to Spring Security filter chain execution.
 * </p>
 * If fine-grained ordering control is required for a custom Filter bean before or after an existing Ultra filter
 * bean, refer to the order value used by the target bean and adjust your bean's order appropriately.
 *
 * @author Jeff Fischer
 */
public class FilterOrdered {

    public static final int PRE_SECURITY_HIGH = -1000000;
    public static final int PRE_SECURITY_MEDIUM = -500000;
    public static final int PRE_SECURITY_LOW = -10000;
    public static final int POST_SECURITY_HIGH = 10000;
    public static final int POST_SECURITY_MEDIUM = 500000;
    public static final int POST_SECURITY_LOW = 1000000;

}
