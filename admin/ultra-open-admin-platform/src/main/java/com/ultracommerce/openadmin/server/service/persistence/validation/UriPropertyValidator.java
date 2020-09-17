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
package com.ultracommerce.openadmin.server.service.persistence.validation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.openadmin.dto.BasicFieldMetadata;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * Validates a field as being a valid URI to ensure compatibility with Ultra handlers including
 * PageHandler, ProductHandler, and CategoryHandlers.
 * 
 * Component can be overridden with the following properties:
 * 
 * This component was introduced instead of using RegEx because most site have simple url needs and UC out of 
 * box simply requires that the URL start with a / and use valid url characters.
 * 
 * Replace if needed for your implementation.
 * 
 * 
 * @author Brian Polster
 */
@Component("ucUriPropertyValidator")
public class UriPropertyValidator extends ValidationConfigurationBasedPropertyValidator {

    protected static final Log LOG = LogFactory.getLog(UriPropertyValidator.class);

    protected String ERROR_KEY_BEGIN_WITH_SLASH = "uriPropertyValidatorMustBeginWithSlashError";
    protected String ERROR_KEY_CANNOT_END_WITH_SLASH = "uriPropertyValidatorCannotEndWithSlashError";
    protected String ERROR_KEY_CANNOT_CONTAIN_SPACES = "uriPropertyValidatorCannotContainSpacesError";

    protected boolean getIgnoreFullUrls() {
        return UCSystemProperty.resolveBooleanSystemProperty("uriPropertyValidator.ignoreFullUrls");
    }

    protected boolean getRequireLeadingSlash() {
        return UCSystemProperty.resolveBooleanSystemProperty("uriPropertyValidator.requireLeadingSlash");
    }

    protected boolean getAllowTrailingSlash() {
        return UCSystemProperty.resolveBooleanSystemProperty("uriPropertyValidator.allowTrailingSlash");
    }

    public boolean isFullUrl(String url) {
        return (url.startsWith("http") || url.startsWith("ftp"));
    }

    /**
     * Denotes what should occur when this validator encounters a null value to validate against. Default behavior is to
     * allow them, which means that this validator will always return true with null values
     */
    protected boolean succeedForNullValues = true;
    
    @Override
    public PropertyValidationResult validate(Entity entity,
            Serializable instance,
            Map<String, FieldMetadata> entityFieldMetadata,
            Map<String, String> validationConfiguration,
            BasicFieldMetadata propertyMetadata,
            String propertyName,
            String value) {
        
        if (value == null) {
            return new PropertyValidationResult(succeedForNullValues);
        }

        if (value.contains(" ")) {
            return new PropertyValidationResult(false, ERROR_KEY_CANNOT_CONTAIN_SPACES);
        }
        
        if (isFullUrl(value) && getIgnoreFullUrls()) {
            return new PropertyValidationResult(true);
        }

        if (getRequireLeadingSlash() && !value.startsWith("/")) {
            return new PropertyValidationResult(false, ERROR_KEY_BEGIN_WITH_SLASH);
        }

        if (!getAllowTrailingSlash() && value.endsWith("/") && value.length() > 1) {
            return new PropertyValidationResult(false, ERROR_KEY_CANNOT_END_WITH_SLASH);
        }

        return new PropertyValidationResult(true);
    }
    
    public boolean isSucceedForNullValues() {
        return succeedForNullValues;
    }

    public void setSucceedForNullValues(boolean succeedForNullValues) {
        this.succeedForNullValues = succeedForNullValues;
    }
}
