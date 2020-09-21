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
package com.ultracommerce.common.util.dao;

/**
 * Specify the attributes of a JOIN that should appear in the TypedQuery. Generally takes the form of:
 * </p>
 * <pre>
 * {@code
 * TypedQueryBuilder builder = new TypedQueryBuilder(com.MyClass, "item")
     .addJoin(new TQJoin("item.collection", "collection"))
     .addRestriction("collection.id", "=", 1L);
 * }
 * </pre>
 * </p>
 * The alias value can be used in subsequent restriction expressions.
 *
 * @author Jeff Fischer
 */
public class TQJoin {

    protected String expression;
    protected String alias;

    public TQJoin(String expression, String alias) {
        this.expression = expression;
        this.alias = alias;
    }

    public String toQl() {
        StringBuilder sb = new StringBuilder();
        sb.append(expression);
        sb.append(" ");
        sb.append(alias);

        return sb.toString();
    }
}
