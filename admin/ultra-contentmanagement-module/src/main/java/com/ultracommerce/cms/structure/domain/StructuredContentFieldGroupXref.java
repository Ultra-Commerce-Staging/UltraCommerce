/*
 * #%L
 * UltraCommerce CMS Module
 * %%
 * Copyright (C) 2009 - 2015 Ultra Commerce
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
/**
 * 
 */
package com.ultracommerce.cms.structure.domain;

import com.ultracommerce.cms.field.domain.FieldGroup;
import com.ultracommerce.common.copy.MultiTenantCloneable;

import java.io.Serializable;



/**
 * <p>
 * XREF entity between a {@link StructuredContentFieldTemplate} and a {@link FieldGroup}
 * 
 * <p>
 * This was created to facilitate specifying ordering for the {@link FieldGroup}s within a {@link StructuredContentFieldTemplate}
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public interface StructuredContentFieldGroupXref extends Serializable, MultiTenantCloneable<StructuredContentFieldGroupXref> {

    /**
     * The order that this field group should have within this template
     */
    Integer getGroupOrder();

    void setGroupOrder(Integer groupOrder);

    StructuredContentFieldTemplate getTemplate();

    void setTemplate(StructuredContentFieldTemplate template);

    FieldGroup getFieldGroup();
    
    void setFieldGroup(FieldGroup fieldGroup);
    
}
