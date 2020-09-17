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
package com.ultracommerce.common.site.domain;

import com.ultracommerce.common.presentation.AdminGroupPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.AdminTabPresentation;

/**
 * Control tab layout and grouping for the SiteImpl entity
 *
 * @author Jeff Fischer
 */
@AdminPresentationClass(friendlyName = "baseSite",
    tabs = {
        @AdminTabPresentation(name = SiteAdminPresentation.TabName.General,
            order = SiteAdminPresentation.TabOrder.General,
            groups = {
                @AdminGroupPresentation(name = SiteAdminPresentation.GroupName.General,
                    order = SiteAdminPresentation.GroupOrder.General,
                    untitled = true),
                @AdminGroupPresentation(name = SiteAdminPresentation.GroupName.Security,
                    order = SiteAdminPresentation.GroupOrder.Security)
            }
        )
    }
)
public interface SiteAdminPresentation {

    public static class TabName {
        public static final String General = "General";
    }

    public static class TabOrder {
        public static final int General = 1000;
    }

    public static class GroupName {
        public static final String General = "General";
        public static final String Security = "SiteImpl_Security_Description";
    }

    public static class GroupOrder {
        public static final int General = 1000;
        public static final int Security = 4000;
    }

}
