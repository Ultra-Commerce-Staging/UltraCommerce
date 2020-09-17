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
package com.ultracommerce.openadmin.web.form.component;


/**
 * View class for the icon that can potentially appear on list grid rows.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class ListGridRecordIcon {
    
    protected String cssClass;
    protected String message;
    protected Boolean hasDetails;
    
    public ListGridRecordIcon withCssClass(String cssClass) {
        setCssClass(cssClass);
        return this;
    }

    public ListGridRecordIcon withMessage(String message) {
        setMessage(message);
        return this;
    }
    
    public ListGridRecordIcon withHasDetails(Boolean hasDetails) {
        setHasDetails(hasDetails);
        return this;
    }

    public String getCssClass() {
        return cssClass;
    }
    
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Boolean getHasDetails() {
        return hasDetails;
    }
    
    public void setHasDetails(Boolean hasDetails) {
        this.hasDetails = hasDetails;
    }
}
