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
package com.ultracommerce.core.catalog.domain;

import com.ultracommerce.common.presentation.AdminGroupPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.AdminTabPresentation;
import com.ultracommerce.common.presentation.PopulateToOneFieldsEnum;

/**
 * @author Jon Fleschler (jfleschler)
 */
@AdminPresentationClass(friendlyName = "Product Option Value",
    tabs = {
        @AdminTabPresentation(name = ProductOptionValueAdminPresentation.TabName.General,
            order = ProductOptionValueAdminPresentation.TabOrder.General,
            groups = {
                @AdminGroupPresentation(name = ProductOptionValueAdminPresentation.GroupName.General,
                    order = ProductOptionValueAdminPresentation.GroupOrder.General,
                    untitled = true)
            }
        )
    }
)

public interface ProductOptionValueAdminPresentation {

    public static class TabName {
        public static final String General = "General";
    }

    public static class TabOrder {
        public static final int General = 1000;
    }

    public static class GroupName {
        public static final String General = "General";
    }

    public static class GroupOrder {
        public static final int General = 1000;
    }

    public static class FieldOrder {
        public static final int value = 1000;
        public static final int adjustment = 2000;
        public static final int order = 3000;
    }
}
