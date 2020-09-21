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
package com.ultracommerce.common.logging;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public abstract class AbstractSupportLoggerAdapter {

    public static final String TRACE = "TRACE";
    public static final String DEBUG = "DEBUG";
    public static final String INFO = "INFO";
    public static final String WARN = "WARN";
    public static final String ERROR = "ERROR";
    public static final String FATAL = "FATAL";
    public static final String SUPPORT = "SUPPORT";

    public static final int LOG_LEVEL_TRACE = 0;
    public static final int LOG_LEVEL_DEBUG = 10;
    public static final int LOG_LEVEL_INFO = 20;
    public static final int LOG_LEVEL_WARN = 30;
    public static final int LOG_LEVEL_ERROR = 40;
    public static final int LOG_LEVEL_FATAL = 50;
    public static final int LOG_LEVEL_SUPPORT = 60;

}
