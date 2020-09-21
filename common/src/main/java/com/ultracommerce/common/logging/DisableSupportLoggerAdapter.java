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
 * <p>An implementation of SupportLoggerAdapter that would disable SupportLogger logging. (i.e. do nothing)</p>
 * @author Elbert Bautista (elbertbautista)
 */
public class DisableSupportLoggerAdapter implements SupportLoggerAdapter {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
        //do nothing
    }

    @Override
    public void support(String message) {
        //do nothing
    }

    @Override
    public void support(String message, Throwable t) {
        //do nothing
    }

    @Override
    public void lifecycle(LifeCycleEvent lifeCycleEvent, String message) {
        //do nothing
    }

    @Override
    public void debug(String message) {
        //do nothing
    }

    @Override
    public void debug(String message, Throwable t) {
        //do nothing
    }

    @Override
    public void error(String message) {
        //do nothing
    }

    @Override
    public void error(String message, Throwable t) {
        //do nothing
    }

    @Override
    public void fatal(String message) {
        //do nothing
    }

    @Override
    public void fatal(String message, Throwable t) {
        //do nothing
    }

    @Override
    public void info(String message) {
        //do nothing
    }

    @Override
    public void info(String message, Throwable t) {
        //do nothing
    }

    @Override
    public void warn(String message) {
        //do nothing
    }

    @Override
    public void warn(String message, Throwable t) {
        //do nothing
    }

}
