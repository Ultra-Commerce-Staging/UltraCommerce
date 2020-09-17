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
package com.ultracommerce.common.extensibility.context.merge;

import org.springframework.core.PriorityOrdered;

/**
 * Use this merge bean post processor for merging tasks that should take place before the persistence layer is
 * initialized. This would include adding class transformers for load time weaving, and the like. See
 * {@link AbstractMergeBeanPostProcessor} for usage information.
 *
 * @see AbstractMergeBeanPostProcessor
 * @author Jeff Fischer
 */
public class EarlyStageMergeBeanPostProcessor extends AbstractMergeBeanPostProcessor implements PriorityOrdered {

    protected int order = Integer.MIN_VALUE;

    /**
     * This is the priority order for this post processor and will determine when this processor is run in relation
     * to other priority ordered processors (e.g. {@link org.springframework.context.annotation.CommonAnnotationBeanPostProcessor})
     * The default value if Integer.MIN_VALUE.
     */
    @Override
    public int getOrder() {
        return order;
    }

    /**
     * This is the priority order for this post processor and will determine when this processor is run in relation
     * to other priority ordered processors (e.g. {@link org.springframework.context.annotation.CommonAnnotationBeanPostProcessor})
     * The default value if Integer.MIN_VALUE.
     *
     * @param order the priority ordering
     */
    public void setOrder(int order) {
        this.order = order;
    }

}
