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
package com.ultracommerce.common.extensibility.context.merge.handlers;

/**
 * Convenience base class which all handler implementations extend. This class
 * provides the common properties required by all MergeHandler implemenations.
 * @author jfischer
 */
public abstract class BaseHandler implements MergeHandler, Comparable<Object> {

    protected int priority;
    protected String xpath;
    protected MergeHandler[] children = {};
    protected String name;

    public int getPriority() {
        return priority;
    }

    public String getXPath() {
        return xpath;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setXPath(String xpath) {
        this.xpath = xpath;
    }

    public int compareTo(Object arg0) {
        return new Integer(getPriority()).compareTo(new Integer(((MergeHandler) arg0).getPriority()));
    }

    public MergeHandler[] getChildren() {
        return children;
    }

    public void setChildren(MergeHandler[] children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
