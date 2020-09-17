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
package com.ultracommerce.openadmin.web.rulebuilder;

import com.ultracommerce.common.util.FormatUtil;
import com.ultracommerce.common.web.UltraRequestContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Work with dates in rule builder mvel
 *
 * @author Jeff Fischer
 */
public class RuleBuilderFormatUtil {

    public static final String COMPATIBILITY_FORMAT = "MM/dd/yy HH:mm a Z";
    public static final String DATE_FORMAT = "MM/dd/yyyy HH:mm";

    /**
     * Prepare date for display in the admin
     *
     * @param date the date to convert
     * @return the string value to show in the admin
     */
    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        formatter.setTimeZone(UltraRequestContext.getUltraRequestContext().getTimeZone());
        return formatter.format(date);
    }

    /**
     * Parse the string value of the date stored in mvel
     *
     * @param date the mvel date value
     * @return the parsed Date instance
     */
    public static Date parseDate(String date) throws ParseException {
        Date parsedDate;
        try {
            parsedDate = FormatUtil.getTimeZoneFormat().parse(date);
        } catch (ParseException e) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(COMPATIBILITY_FORMAT);
                formatter.setTimeZone(UltraRequestContext.getUltraRequestContext().getTimeZone());
                parsedDate = formatter.parse(date);
            } catch (ParseException e1) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
                    formatter.setTimeZone(UltraRequestContext.getUltraRequestContext().getTimeZone());
                    parsedDate = formatter.parse(date);
                } catch (ParseException e2) {
                    throw e;
                }
            }
        }
        return parsedDate;
    }
}
