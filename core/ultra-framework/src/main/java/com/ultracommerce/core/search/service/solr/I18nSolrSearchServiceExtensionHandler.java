/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.search.service.solr;

import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.i18n.service.TranslationService;
import com.ultracommerce.common.locale.domain.Locale;
import com.ultracommerce.common.locale.service.LocaleService;
import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.search.domain.Field;
import com.ultracommerce.core.search.domain.IndexField;
import com.ultracommerce.core.search.domain.solr.FieldType;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * If the field is translatable, then this method prefixes the field with supported locales.
 * 
 * @author bpolster
 */
@Service("ucI18nSolrSearchServiceExtensionHandler")
public class I18nSolrSearchServiceExtensionHandler extends AbstractSolrSearchServiceExtensionHandler
        implements SolrSearchServiceExtensionHandler {

    @Resource(name = "ucSolrHelperService")
    protected SolrHelperService shs;

    @Resource(name = "ucSolrSearchServiceExtensionManager")
    protected SolrSearchServiceExtensionManager extensionManager;

    @Resource(name = "ucTranslationService")
    protected TranslationService translationService;

    @Resource(name = "ucLocaleService")
    protected LocaleService localeService;

    protected boolean getTranslationEnabled() {
        return UCSystemProperty.resolveBooleanSystemProperty("i18n.translation.enabled");
    }

    @PostConstruct
    public void init() {
        boolean shouldAdd = true;
        for (SolrSearchServiceExtensionHandler h : extensionManager.getHandlers()) {
            if (h instanceof I18nSolrSearchServiceExtensionHandler) {
                shouldAdd = false;
                break;
            }
        }
        if (shouldAdd) {
            extensionManager.getHandlers().add(this);
        }
    }

    @Override
    public ExtensionResultStatusType buildPrefixListForIndexField(IndexField field, FieldType fieldType, List<String> prefixList) {
        return getLocalePrefix(field.getField(), prefixList);
    }

    /**
     * If the field is translatable, take the current locale and add that as a prefix.
     * @param context
     * @param field
     * @return
     */
    protected ExtensionResultStatusType getLocalePrefix(Field field, List<String> prefixList) {
        if (field.getTranslatable() && getTranslationEnabled()) {
            if (UltraRequestContext.getUltraRequestContext() != null) {
                Locale locale = UltraRequestContext.getUltraRequestContext().getLocale();
                if (locale != null) {
                    String localeCode = locale.getLocaleCode();
                    if (Boolean.FALSE.equals(locale.getUseCountryInSearchIndex())) {
                        int pos = localeCode.indexOf("_");
                        if (pos > 0) {
                            localeCode = localeCode.substring(0, pos);
                        }
                    }
                    prefixList.add(localeCode);
                    return ExtensionResultStatusType.HANDLED_CONTINUE;
                }
            }
        }

        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public int getPriority() {
        return 1000;
    }
}
