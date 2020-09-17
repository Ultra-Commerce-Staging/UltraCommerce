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
package com.ultracommerce.common.presentation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes additional filter parameters used to refine the list of items returned from a query for
 * a DataDrivenEnumeration
 *
 * @author Jeff Fischer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface OptionFilterParam {

    /**
     * <p>The field name in the target entity class that should be used to refine the query (i.e. sql where clause). The
     * param can be "." delimited in standard bean property fashion. For example, the preferred way of referring to
     * DataDrivenEnumerationValueImpl instances belonging to a particular instance of DataDrivenEnumerationImpl is by
     * specifying the param value as follows:</p>
     *
     * <p>param="type.key"</p>
     *
     * @see com.ultracommerce.common.enumeration.domain.DataDrivenEnumerationValueImpl
     * @return the field name with which to refine the query
     */
    String param();

    /**
     * <p>The field value that should match for any items returned from the query</p>
     *
     * @return the field match value
     */
    String value();

    /**
     * <p>This is the type for the value stored in this OptionFilterParam annotation. The system will use this type
     * to properly convert the String value to the correct type when executing the query.</p>
     *
     * @return the final type for the param value
     */
    OptionFilterParamType paramType();

}
