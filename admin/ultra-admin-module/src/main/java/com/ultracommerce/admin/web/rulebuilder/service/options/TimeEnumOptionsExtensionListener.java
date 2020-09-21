/*
 * #%L
 * UltraCommerce Admin Module
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
package com.ultracommerce.admin.web.rulebuilder.service.options;

import com.ultracommerce.common.UltraEnumerationType;
import com.ultracommerce.common.time.DayOfMonthType;
import com.ultracommerce.common.time.DayOfWeekType;
import com.ultracommerce.common.time.HourOfDayType;
import com.ultracommerce.common.time.MinuteType;
import com.ultracommerce.common.time.MonthType;
import com.ultracommerce.openadmin.web.rulebuilder.enums.AbstractRuleBuilderEnumOptionsExtensionListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Rule Builder enum options provider for {@link HourOfDayType}
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Component("ucTimeOptionsExtensionListener")
public class TimeEnumOptionsExtensionListener extends AbstractRuleBuilderEnumOptionsExtensionListener {

    @Override
    protected Map<String, Class<? extends UltraEnumerationType>> getValuesToGenerate() {
        Map<String, Class<? extends UltraEnumerationType>> map = 
                new HashMap<String, Class<? extends UltraEnumerationType>>();
        
        map.put("ucOptions_HourOfDay", HourOfDayType.class);
        map.put("ucOptions_DayOfWeek", DayOfWeekType.class);
        map.put("ucOptions_Month", MonthType.class);
        map.put("ucOptions_DayOfMonth", DayOfMonthType.class);
        map.put("ucOptions_Minute", MinuteType.class);
        
        return map;
    }

}
