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

import org.w3c.dom.Node;

import java.util.List;

/**
 * This adapter class allows the developer to create a merge handler
 * instance and only override a subset of the functionality, instead of
 * having to provide an independent, full implementation of the MergeHandler
 * interface.
 * 
 * @author jfischer
 *
 */
public class MergeHandlerAdapter implements MergeHandler {

    public MergeHandler[] getChildren() {
        return null;
    }

    public String getName() {
        return null;
    }

    public int getPriority() {
        return 0;
    }

    public String getXPath() {
        return null;
    }

    public Node[] merge(List<Node> nodeList1, List<Node> nodeList2,
            List<Node> exhaustedNodes) {
        return null;
    }

    public void setChildren(MergeHandler[] children) {
        //do nothing
    }

    public void setName(String name) {
        //do nothing
    }

    public void setPriority(int priority) {
        //do nothing
    }

    public void setXPath(String xpath) {
        //do nothing
    }

}
