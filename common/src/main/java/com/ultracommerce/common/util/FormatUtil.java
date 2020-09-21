/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.common.util;

import com.ultracommerce.common.web.UltraRequestContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;

/**
 * @author Jeff Fischer
 */
public class FormatUtil {

    public static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
    public static final String DATE_FORMAT_WITH_TIMEZONE = "yyyy.MM.dd HH:mm:ss Z";

    public static SimpleDateFormat getDateFormat() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        formatter.setTimeZone(UltraRequestContext.getUltraRequestContext().getTimeZone());
        return formatter;
    }
    
    /**
     * Used with dates in rules since they are not stored as a Timestamp type (and thus not converted to a specific database
     * timezone on a save). In order to provide accurate information, the timezone must also be preserved in the MVEL rule
     * expression
     * 
     * @return
     */
    public static SimpleDateFormat getTimeZoneFormat() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_WITH_TIMEZONE);
        formatter.setTimeZone(UltraRequestContext.getUltraRequestContext().getTimeZone());
        return formatter;
    }

    /**
     * Use to produce date Strings in the W3C date format
     * 
     * @param date
     * @return
     * @throws DatatypeConfigurationException 
     */
    public static String formatDateUsingW3C(Date date) {
        String w3cDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
        return w3cDate = w3cDate.substring(0, 22) + ":" + w3cDate.substring(22, 24);
    }

}
