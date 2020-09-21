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
public interface SupportLoggerAdapter {

    public String getName();

    public void setName(String name);

    /**
     * emit a SUPPORT level message
     * @param message
     */
    public void support(String message);

    /**
     * emit a SUPPORT level message with throwable
     * @param message
     * @param t
     */
    public void support(String message, Throwable t);

    /**
     * emit a SUPPORT lifecycle message
     * @param lifeCycleEvent
     * @param message
     */
    public void lifecycle(LifeCycleEvent lifeCycleEvent, String message);

    /**
     * In order to be backwards compatible. The support logger should also support
     * the debug, error, fatal, info, and warn levels as well.
     * @param message
     */

    public void debug(String message);

    public void debug(String message, Throwable t);

    public void error(String message);

    public void error(String message, Throwable t);

    public void fatal(String message);

    public void fatal(String message, Throwable t);

    public void info(String message);

    public void info(String message, Throwable t);

    public void warn(String message);

    public void warn(String message, Throwable t);

}
