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
package com.ultracommerce.cms.url.domain;

import com.ultracommerce.common.presentation.AdminGroupPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.AdminTabPresentation;
import com.ultracommerce.common.presentation.PopulateToOneFieldsEnum;

/**
 * @author Jon Fleschler (jfleschler)
 */
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "URLHandlerImpl_friendyName",
        tabs = {
                @AdminTabPresentation(name = URLHandlerAdminPresentation.TabName.General,
                        order = URLHandlerAdminPresentation.TabOrder.General,
                        groups = {
                                @AdminGroupPresentation(name = URLHandlerAdminPresentation.GroupName.General,
                                        order = URLHandlerAdminPresentation.GroupOrder.General,
                                        untitled = true),
                                @AdminGroupPresentation(name = URLHandlerAdminPresentation.GroupName.Redirect,
                                        order = URLHandlerAdminPresentation.GroupOrder.Redirect,
                                        column = 1)
                        }
                )
        }
)

public interface URLHandlerAdminPresentation {


    public static class TabName {
        public static final String General = "General";
    }

    public static class TabOrder {
        public static final int General = 1000;
    }

    public static class GroupName {
        public static final String General = "General";
        public static final String Redirect = "URLHandlerImpl_group_Redirect";
    }

    public static class GroupOrder {
        public static final int General = 1000;
        public static final int Redirect = 2000;
    }
}
