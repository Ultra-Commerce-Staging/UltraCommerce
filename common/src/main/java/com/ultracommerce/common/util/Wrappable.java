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
package com.ultracommerce.common.util;

/**
 * Utility interface for items that wrap an internal, delegate item
 *
 * @author Jeff Fischer
 */
public interface Wrappable {

    /**
     * Can this wrapped item be unwrapped as the indicated type?
     *
     * @param unwrapType The type to check.
     * @return True/false.
     */
    public boolean isUnwrappableAs(Class unwrapType);

    /**
     * Get the wrapped delegate item
     *
     * @param unwrapType The java type as which to unwrap this instance.
     * @return The unwrapped reference
     */
    public <T> T unwrap(Class<T> unwrapType);

}
