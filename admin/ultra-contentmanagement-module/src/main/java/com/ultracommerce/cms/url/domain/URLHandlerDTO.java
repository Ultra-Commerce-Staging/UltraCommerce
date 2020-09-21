/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.url.domain;

import com.ultracommerce.cms.url.type.URLRedirectType;
import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;


/**
 * A bean representation of a URLHandler
 *
 * @author bpolster
 */
public class URLHandlerDTO implements URLHandler {

    private static final long serialVersionUID = 1L;
    protected Long id = null;
    protected String incomingURL = "";
    protected String newURL;
    protected String urlRedirectType;
    protected boolean isRegex = false;

    public URLHandlerDTO(String newUrl, URLRedirectType redirectType) {
        setUrlRedirectType(redirectType);
        setNewURL(newUrl);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getIncomingURL() {
        return incomingURL;
    }

    @Override
    public void setIncomingURL(String incomingURL) {
        this.incomingURL = incomingURL;
    }

    @Override
    public String getNewURL() {
        return newURL;
    }

    @Override
    public void setNewURL(String newURL) {
        this.newURL = newURL;
    }

    @Override
    public boolean isRegexHandler() {
        return isRegex;
    }

    /**
     * @Deprecated use {@link #setRegexHandler(Boolean regexHandler)}
     */
    @Deprecated
    @Override
    public void setRegexHandler(boolean regexHandler) {
        this.isRegex = regexHandler;
    }

    @Override
    public void setRegexHandler(Boolean regexHandler) {
        this.isRegex = regexHandler != null ? regexHandler : false;
    }

    @Override
    public URLRedirectType getUrlRedirectType() {
        return URLRedirectType.getInstance(urlRedirectType);
    }

    @Override
    public void setUrlRedirectType(URLRedirectType redirectType) {
        this.urlRedirectType = redirectType == null ? null : redirectType.getType();
    }

    @Override
    public <G extends URLHandler> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        URLHandler cloned = createResponse.getClone();
        cloned.setIncomingURL(incomingURL);
        cloned.setNewURL(newURL);
        cloned.setUrlRedirectType(URLRedirectType.getInstance(urlRedirectType));
        cloned.setRegexHandler(new Boolean(isRegex));
        return createResponse;
    }
}
