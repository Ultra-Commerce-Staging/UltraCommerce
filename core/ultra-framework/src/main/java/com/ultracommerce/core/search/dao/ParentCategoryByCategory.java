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
package com.ultracommerce.core.search.dao;

/**
 * Container object for the results from a lightweight query that retrieves parent categories
 * for a child category
 *
 * @author Jeff Fischer
 */
public class ParentCategoryByCategory {

    protected Long parent;
    protected Long defaultParent;
    protected Long child;

    public ParentCategoryByCategory(Long parent, Long defaultParent, Long child) {
        this.parent = parent;
        this.defaultParent = defaultParent;
        this.child = child;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Long getDefaultParent() {
        return defaultParent;
    }

    public void setDefaultParent(Long defaultParent) {
        this.defaultParent = defaultParent;
    }

    public Long getChild() {
        return child;
    }

    public void setChild(Long child) {
        this.child = child;
    }
}
