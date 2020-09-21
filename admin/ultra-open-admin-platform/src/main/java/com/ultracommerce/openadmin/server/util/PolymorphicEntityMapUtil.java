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
package com.ultracommerce.openadmin.server.util;

import com.ultracommerce.openadmin.dto.ClassTree;

import java.util.LinkedHashMap;

/**
 * @author Elbert Bautista (elbertbautista)
 *
 * Utility class to convert the Polymorphic ClassTree into a Map
 */
public class PolymorphicEntityMapUtil {

    public LinkedHashMap<String, String> convertClassTreeToMap(ClassTree polymorphicEntityTree) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(polymorphicEntityTree.getRight()/2);
        buildPolymorphicEntityMap(polymorphicEntityTree, map);
        return map;
    }

    protected void buildPolymorphicEntityMap(ClassTree entity, LinkedHashMap<String, String> map) {
        String friendlyName = entity.getFriendlyName();
        if (friendlyName != null && !friendlyName.equals("")) {

            //TODO: fix this for i18N
            //check if the friendly name is an i18N key
            //String val = UCMain.getMessageManager().getString(friendlyName);
            //if (val != null) {
            //    friendlyName = val;
            //}

        }
        map.put(entity.getFullyQualifiedClassname(), friendlyName!=null?friendlyName:entity.getName());
        for (ClassTree child : entity.getChildren()) {
            buildPolymorphicEntityMap(child, map);
        }
    }

}
