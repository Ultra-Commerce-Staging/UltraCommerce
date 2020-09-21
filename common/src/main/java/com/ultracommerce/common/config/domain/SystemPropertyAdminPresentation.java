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
package com.ultracommerce.common.config.domain;

import com.ultracommerce.common.presentation.AdminGroupPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.AdminTabPresentation;

@AdminPresentationClass(friendlyName = "SystemPropertyImpl",
    tabs = {
        @AdminTabPresentation(
            groups = {
                @AdminGroupPresentation(name = SystemPropertyAdminPresentation.GroupName.General,
                    order = SystemPropertyAdminPresentation.GroupOrder.General,
                    untitled = true),
                @AdminGroupPresentation(name = SystemPropertyAdminPresentation.GroupName.Placement,
                    order = SystemPropertyAdminPresentation.GroupOrder.Placement,
                    column = 1)
            }
        )
    }
)
public interface SystemPropertyAdminPresentation {

    public static class TabName {
    }

    public static class TabOrder {
    }

    public static class GroupName {
        public static final String General = "General";
        public static final String Placement = "SystemPropertyImpl_placement";
    }

    public static class GroupOrder {
        public static final int General = 1000;
        public static final int Placement = 2000;
    }

    public static class FieldOrder {
        public static final int FRIENDLY_NAME = 1000;
        public static final int ATTRIBUTE_NAME = 2000;
        public static final int PROPERTY_TYPE = 3000;
        public static final int VALUE = 4000;

        public static final int GROUP_NAME = 1000;
        public static final int TAB_NAME = 2000;
    }

}
