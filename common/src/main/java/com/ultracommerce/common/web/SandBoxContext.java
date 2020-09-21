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
package com.ultracommerce.common.web;


import com.ultracommerce.common.classloader.release.ThreadLocalManager;

public class SandBoxContext {
    
    private static final ThreadLocal<SandBoxContext> SANDBOXCONTEXT = ThreadLocalManager.createThreadLocal(SandBoxContext.class);
    
    public static SandBoxContext getSandBoxContext() {
        return SANDBOXCONTEXT.get();
    }
    
    public static void setSandBoxContext(SandBoxContext sandBoxContext) {
        SANDBOXCONTEXT.set(sandBoxContext);
    }

    protected Long sandBoxId;
    protected Boolean previewMode = false;

    /**
     * @return the sandBoxName
     */
    public Long getSandBoxId() {
        return sandBoxId;
    }
    
    /**
     * @param sandBoxId the sandBoxName to set
     */
    public void setSandBoxId(Long sandBoxId) {
        this.sandBoxId = sandBoxId;
    }

    public Boolean getPreviewMode() {
        return previewMode;
    }

    public void setPreviewMode(Boolean previewMode) {
        this.previewMode = previewMode;
    }

    public SandBoxContext clone() {
        SandBoxContext myContext = new SandBoxContext();
        myContext.setSandBoxId(getSandBoxId());
        myContext.setPreviewMode(getPreviewMode());

        return myContext;
    }
}
