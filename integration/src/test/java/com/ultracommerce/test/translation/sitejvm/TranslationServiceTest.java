/*
 * #%L
 * UltraCommerce Integration
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
package com.ultracommerce.test.translation.sitejvm;

import com.ultracommerce.common.i18n.domain.TranslatedEntity;
import com.ultracommerce.common.i18n.service.TranslationService;
import com.ultracommerce.common.locale.domain.LocaleImpl;
import com.ultracommerce.common.locale.service.LocaleService;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.CategoryImpl;
import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.test.TestNGSiteIntegrationSetup;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Locale;

import javax.annotation.Resource;

/**
 * Test basic core translation use cases. Confirms core is configured correctly and operational.
 *
 * @author Jeff Fischer
 */
public class TranslationServiceTest extends TestNGSiteIntegrationSetup {

    @Resource
    private LocaleService localeService;

    @Resource
    private TranslationService translationService;

    @Resource
    private CatalogService catalogService;

    @Test(groups = {"testTranslation"})
    @Transactional
    public void testTranslation() throws Exception {
        Category category = new CategoryImpl();
        category.setName("Translation");
        category = catalogService.saveCategory(category);

        com.ultracommerce.common.locale.domain.Locale esLocale = new LocaleImpl();
        esLocale.setLocaleCode("es");
        localeService.save(esLocale);

        com.ultracommerce.common.locale.domain.Locale esMxLocale = new LocaleImpl();
        esMxLocale.setLocaleCode("es_MX");
        localeService.save(esMxLocale);

        translationService.save(TranslatedEntity.CATEGORY.getType(), String.valueOf(category.getId()), "name", "es_MX", "es_MX");
        translationService.save(TranslatedEntity.CATEGORY.getType(), String.valueOf(category.getId()), "name", "es", "es");

        String specificTranslation = translationService.getTranslatedValue(category, "name", new Locale("es", "MX"));
        Assert.assertEquals(specificTranslation, "es_MX");

        String generalTranslation = translationService.getTranslatedValue(category, "name", Locale.forLanguageTag("es"));
        Assert.assertEquals(generalTranslation, "es");

        //test a second time to go through cache

        specificTranslation = translationService.getTranslatedValue(category, "name", new Locale("es", "MX"));
        Assert.assertEquals(specificTranslation, "es_MX");

        generalTranslation = translationService.getTranslatedValue(category, "name", Locale.forLanguageTag("es"));
        Assert.assertEquals(generalTranslation, "es");
    }
}
