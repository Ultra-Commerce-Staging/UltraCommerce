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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.ultracommerce.cms.url.type.URLRedirectType;
import com.ultracommerce.common.admin.domain.AdminMainEntity;
import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.extensibility.jpa.copy.ProfileEntity;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.PopulateToOneFieldsEnum;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.common.presentation.client.VisibilityEnum;
import com.ultracommerce.common.web.Locatable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;


/**
 * @author priyeshpatel
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "UC_URL_HANDLER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ucStandardElements")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "URLHandlerImpl_friendyName")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps = true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class URLHandlerImpl implements URLHandler, Locatable, AdminMainEntity, ProfileEntity, URLHandlerAdminPresentation {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "URLHandlerID")
    @GenericGenerator(
            name = "URLHandlerID",
            strategy = "com.ultracommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @Parameter(name = "segment_value", value = "URLHandlerImpl"),
                    @Parameter(name = "entity_name", value = "com.ultracommerce.cms.url.domain.URLHandlerImpl")
            }
    )
    @Column(name = "URL_HANDLER_ID")
    @AdminPresentation(friendlyName = "URLHandlerImpl_ID", order = 1, group = GroupName.General, visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @AdminPresentation(friendlyName = "URLHandlerImpl_incomingURL", order = 1, group = GroupName.General, prominent = true,
            helpText = "urlHandlerIncoming_help", defaultValue = "")
    @Column(name = "INCOMING_URL", nullable = false)
    @Index(name = "INCOMING_URL_INDEX", columnNames = {"INCOMING_URL"})
    protected String incomingURL;

    @Column(name = "NEW_URL", nullable = false)
    @AdminPresentation(friendlyName = "URLHandlerImpl_newURL", order = 1, group = GroupName.General, prominent = true,
            helpText = "urlHandlerNew_help", defaultValue = "")
    protected String newURL;

    @Column(name = "URL_REDIRECT_TYPE")
    @AdminPresentation(friendlyName = "URLHandlerImpl_redirectType", order = 4, group = GroupName.Redirect,
            fieldType = SupportedFieldType.ULTRA_ENUMERATION,
            ultraEnumeration = "com.ultracommerce.cms.url.type.URLRedirectType",
            prominent = true)
    protected String urlRedirectType;

    @Column(name = "IS_REGEX")
    @AdminPresentation(friendlyName = "URLHandlerImpl_isRegexHandler", order = 1, group = GroupName.General, prominent = true,
            groupOrder = 1,
            helpText = "urlHandlerIsRegexHandler_help")
    @Index(name = "IS_REGEX_HANDLER_INDEX", columnNames = {"IS_REGEX"})
    protected Boolean isRegex = false;

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
    public URLRedirectType getUrlRedirectType() {
        return URLRedirectType.getInstance(urlRedirectType);
    }

    @Override
    public void setUrlRedirectType(URLRedirectType redirectType) {
        this.urlRedirectType = redirectType.getType();
    }

    @Override
    public boolean isRegexHandler() {
        if (isRegex == null) {
            if (hasRegExCharacters(getIncomingURL())) {
                return true;
            }
            return false;
        }
        return isRegex;
    }

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
    public String getMainEntityName() {
        return getIncomingURL();
    }

    @Override
    public String getLocation() {
        String location = getIncomingURL();
        if (location == null) {
            return null;
        } else if (isRegexHandler()) {
            return getNewURL();
        } else {
            return location;
        }
    }

    /**
     * In a preview environment, {@link #getLocation()} attempts to navigate to the
     * provided URL.    If the URL contains a Regular Expression, then we can't
     * navigate to it.
     *
     * @param location
     * @return
     */
    protected boolean hasRegExCharacters(String location) {
        return location.contains(".") ||
                location.contains("(") ||
                location.contains(")") ||
                location.contains("?") ||
                location.contains("*") ||
                location.contains("^") ||
                location.contains("$") ||
                location.contains("[") ||
                location.contains("{") ||
                location.contains("|") ||
                location.contains("+") ||
                location.contains("\\");
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
        cloned.setRegexHandler(isRegex==null?false:isRegex);
        return createResponse;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        URLHandlerImpl rhs = (URLHandlerImpl) obj;
        return new EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.incomingURL, rhs.incomingURL)
                .append(this.newURL, rhs.newURL)
                .append(this.urlRedirectType, rhs.urlRedirectType)
                .append(this.isRegex, rhs.isRegex)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(incomingURL)
                .append(newURL)
                .append(urlRedirectType)
                .append(isRegex)
                .toHashCode();
    }
}
