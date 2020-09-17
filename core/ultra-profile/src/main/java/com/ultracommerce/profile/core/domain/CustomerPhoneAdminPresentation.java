/*
 * #%L
 * UltraCommerce Profile
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
package com.ultracommerce.profile.core.domain;

import com.ultracommerce.common.presentation.AdminGroupPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.AdminTabPresentation;
import com.ultracommerce.common.presentation.PopulateToOneFieldsEnum;

/**
 * Created by ckittrell on 12/9/15.
 */
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE,
    tabs = {
        @AdminTabPresentation(name = CustomerPhoneAdminPresentation.TabName.General,
            order = CustomerPhoneAdminPresentation.TabOrder.General,
            groups = {
                @AdminGroupPresentation(name = CustomerPhoneAdminPresentation.GroupName.PhoneInfo,
                    order = CustomerPhoneAdminPresentation.GroupOrder.PhoneInfo,
                    untitled = true),
                @AdminGroupPresentation(name = CustomerPhoneAdminPresentation.GroupName.Defaults,
                    order = CustomerPhoneAdminPresentation.GroupOrder.Defaults,
                    column = 1, untitled = true)
            }
        )
    }
)
public interface CustomerPhoneAdminPresentation {
    public static class TabName {
        public static final String General = "General";
    }

    public static class TabOrder {
        public static final int General = 1000;
    }

    public static class GroupName {
        public static final String PhoneInfo = "CustomerPhoneImpl_PhoneInfo_Group";
        public static final String Defaults = "CustomerPhoneImpl_Defaults_Group";
    }

    public static class GroupOrder {
        public static final int PhoneInfo = 1000;
        public static final int Defaults = 2000;
    }

    public static class FieldOrder {
        public static final int PHONE_NAME = 1000;
    }
}
