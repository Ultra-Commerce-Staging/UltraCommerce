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
package com.ultracommerce.common.web.deeplink;

/**
 * DTO Class that contains enough information to allow the client site application to generate
 * the necessary information for a link to an admin screen
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class DeepLink {

    protected String adminBaseUrl;
    protected String urlFragment;
    protected String displayText;
    protected Object sourceObject;

    /* ******************* *
     * WITH FLUID BUILDERS *
     * ******************* */
    
    public DeepLink withAdminBaseUrl(String adminBaseUrl) {
        setAdminBaseUrl(adminBaseUrl);
        return this;
    }

    public DeepLink withUrlFragment(String urlFragment) {
        setUrlFragment(urlFragment);
        return this;
    }

    public DeepLink withDisplayText(String displayText) {
        setDisplayText(displayText);
        return this;
    }
    
    public DeepLink withSourceObject(Object sourceObject) {
        setSourceObject(sourceObject);
        return this;
    }

    /* ************************ *
     * CUSTOM GETTERS / SETTERS *
     * ************************ */

    public void setAdminBaseUrl(String adminBaseUrl) {
        if (adminBaseUrl.charAt(adminBaseUrl.length() - 1) == '/') {
            adminBaseUrl = adminBaseUrl.substring(0, adminBaseUrl.length() - 1);
        }
        this.adminBaseUrl = adminBaseUrl;
    }

    public void setUrlFragment(String urlFragment) {
        if (urlFragment.charAt(0) == '/') {
            urlFragment = urlFragment.substring(1);
        }
        this.urlFragment = urlFragment;
    }
    
    public String getFullUrl() {
        return adminBaseUrl + "/" + urlFragment;
    }

    /* ************************* *
     * GENERIC GETTERS / SETTERS *
     * ************************* */

    public String getAdminBaseUrl() {
        return adminBaseUrl;
    }

    public String getUrlFragment() {
        return urlFragment;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }
    
    public Object getSourceObject() {
        return sourceObject;
    }
    
    public void setSourceObject(Object sourceObject) {
        this.sourceObject = sourceObject;
    }
    
}
