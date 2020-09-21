/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.dto.visitor;

import com.ultracommerce.openadmin.dto.AdornedTargetCollectionMetadata;
import com.ultracommerce.openadmin.dto.BasicCollectionMetadata;
import com.ultracommerce.openadmin.dto.BasicFieldMetadata;
import com.ultracommerce.openadmin.dto.GroupMetadata;
import com.ultracommerce.openadmin.dto.MapMetadata;
import com.ultracommerce.openadmin.dto.TabMetadata;

import java.io.Serializable;

/**
 * @author Jeff Fischer
 */
public class MetadataVisitorAdapter implements MetadataVisitor, Serializable {

    @Override
    public void visit(AdornedTargetCollectionMetadata metadata) {
        throw new IllegalArgumentException("Not supported in this context");
    }

    @Override
    public void visit(BasicFieldMetadata metadata) {
        throw new IllegalArgumentException("Not supported in this context");
    }

    @Override
    public void visit(BasicCollectionMetadata metadata) {
        throw new IllegalArgumentException("Not supported in this context");
    }

    @Override
    public void visit(MapMetadata metadata) {
        throw new IllegalArgumentException("Not supported in this context");
    }

    @Override
    public void visit(GroupMetadata metadata) {
        throw new IllegalArgumentException("Not supported in this context");
    }

    @Override
    public void visit(TabMetadata metadata) {
        throw new IllegalArgumentException("Not supported in this context");
    }
}
