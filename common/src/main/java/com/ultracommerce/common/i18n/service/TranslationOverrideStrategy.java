/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.common.i18n.service;

import com.ultracommerce.common.i18n.domain.TranslatedEntity;
import org.springframework.core.Ordered;

/**
 * Provides translations at both the standard site catalog and template catalog level, whichever is appropriate. This is
 * only meaningful in a multitenant scenario.
 *
 * @author Jeff Fischer
 */
public interface TranslationOverrideStrategy extends Ordered {

    /**
     * Retrieve the standard site override translation, if applicable
     *
     * @param property
     * @param entityType
     * @param entityId
     * @param localeCode
     * @param localeCountryCode
     * @param basicCacheKey
     * @return
     */
    LocalePair getLocaleBasedOverride(String property, TranslatedEntity entityType, String entityId,
                                      String localeCode, String localeCountryCode, String basicCacheKey);

    /**
     * Retrieve the template level translation, if applicable
     *
     * @param templateCacheKey
     * @param property
     * @param entityType
     * @param entityId
     * @param localeCode
     * @param localeCountryCode
     * @param specificPropertyKey
     * @param generalPropertyKey
     * @return
     */
    LocalePair getLocaleBasedTemplateValue(String templateCacheKey, String property, TranslatedEntity entityType,
                                           String entityId, String localeCode, String localeCountryCode, String specificPropertyKey,
                                           String generalPropertyKey);

    /**
     * Whether or not a template version should be searched for. If false, then the system will return null for the
     * translation, should an override not be found.
     *
     * @param standardCacheKey
     * @param templateCacheKey
     * @return
     */
    boolean validateTemplateProcessing(String standardCacheKey, String templateCacheKey);
}
