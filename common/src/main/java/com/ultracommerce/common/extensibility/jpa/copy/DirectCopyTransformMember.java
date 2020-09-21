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
package com.ultracommerce.common.extensibility.jpa.copy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Jeff Fischer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DirectCopyTransformMember {

    String[] templateTokens();

    boolean renameMethodOverlaps() default false;

    /**
     * <p>Defaults to false.</p>
     * <p>skipOverlaps is useful if you want to make sure the load time weaving does not try to insert methods you have
     * already implemented. For example, if you have already implemented the Status interface and methods (e.g. Offer),
     * then you don't want the system to try to overwrite these.</p>
     *
     * @return
     */
    boolean skipOverlaps() default true;

    /**
     * The name that should be used when dynamically generating index names instead of the table name. This is needed
     * when two entities have table names that create the same dynamic index names. Generally the strategy used to create
     * the table name part of the index is to use the first two characters of each word in the table name split on the underscores.
     * 
     * i.e.
     * UC_APPLE_CARROT -> UCAPCA
     * UC_APPLY_CAR    -> UCAPCA
     * 
     * Since both tables have a collision, UC_APPLY_CAR can set this property to "BLAPPCA" to avoid collisions
     * 
     * @return
     */
    String overrideIndexNameKey() default "";

}
