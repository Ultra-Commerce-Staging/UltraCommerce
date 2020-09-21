/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.page.domain;

import com.ultracommerce.common.presentation.AdminGroupPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.AdminTabPresentation;
import com.ultracommerce.common.presentation.PopulateToOneFieldsEnum;

/**
 * Created by brandon on 9/3/15.
 */
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "PageImpl_basePage",
    tabs = {
        @AdminTabPresentation(name = PageAdminPresentation.TabName.General,
            order = PageAdminPresentation.TabOrder.General,
            groups = {
                @AdminGroupPresentation(name = PageAdminPresentation.GroupName.Basic,
                    order = PageAdminPresentation.GroupOrder.Basic),
                @AdminGroupPresentation(name = PageAdminPresentation.GroupName.Data,
                    order = PageAdminPresentation.GroupOrder.Data),
                @AdminGroupPresentation(name = PageAdminPresentation.GroupName.Misc,
                    order = PageAdminPresentation.GroupOrder.Misc,
                    column = 1)
            }
        ),
        @AdminTabPresentation(name = PageAdminPresentation.TabName.Seo,
            order = PageAdminPresentation.TabOrder.Seo,
            groups = {
                @AdminGroupPresentation(name = PageAdminPresentation.GroupName.Tags,
                    order = PageAdminPresentation.GroupOrder.Tags),
                @AdminGroupPresentation(name = PageAdminPresentation.GroupName.Sitemap,
                    order = PageAdminPresentation.GroupOrder.Sitemap,
                    column = 1)
            }
        )
    }
)
public interface PageAdminPresentation {
    public static class TabName {
        public static final String General = "General";
        public static final String Seo = "PageImpl_Seo_Tab";
    }

    public static class TabOrder {
        public static final int General = 1000;
        public static final int Seo = 2000;
    }

    public static class GroupName {
        public static final String Basic = "PageImpl_Basic";
        public static final String Data = "PageImpl_Data";
        public static final String Misc = "PageImpl_Misc";

        public static final String Tags = "PageImpl_MetaData";
        public static final String Sitemap = "PageImpl_SiteMap";
    }

    public static class GroupOrder {
        public static final int Basic = 1000;
        public static final int Data = 2000;
        public static final int Misc = 3000;

        public static final int Tags = 1000;

        public static final int Sitemap = 1000;
    }
}
