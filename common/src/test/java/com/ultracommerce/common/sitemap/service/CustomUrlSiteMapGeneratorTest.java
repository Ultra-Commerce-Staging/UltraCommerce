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

package com.ultracommerce.common.sitemap.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import com.ultracommerce.common.site.domain.Site;
import com.ultracommerce.common.site.domain.SiteImpl;
import com.ultracommerce.common.sitemap.domain.CustomUrlSiteMapGeneratorConfiguration;
import com.ultracommerce.common.sitemap.domain.CustomUrlSiteMapGeneratorConfigurationImpl;
import com.ultracommerce.common.sitemap.domain.SiteMapUrlEntry;
import com.ultracommerce.common.sitemap.domain.SiteMapUrlEntryImpl;
import com.ultracommerce.common.sitemap.exception.SiteMapException;
import com.ultracommerce.common.sitemap.service.CustomUrlSiteMapGenerator;
import com.ultracommerce.common.sitemap.service.type.SiteMapChangeFreqType;
import com.ultracommerce.common.sitemap.service.type.SiteMapGeneratorType;
import com.ultracommerce.common.sitemap.service.type.SiteMapPriorityType;
import com.ultracommerce.common.web.UltraRequestContext;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Custom URL site map generator tests
 * 
 * @author Joshua Skorton (jskorton)
 */
public class CustomUrlSiteMapGeneratorTest extends SiteMapGeneratorTest {

    @Test
    public void testCustomUrlSiteMapGenerator() throws SiteMapException, IOException {
        CustomUrlSiteMapGeneratorConfiguration smgc = getConfiguration();
        testGenerator(smgc, new CustomUrlSiteMapGenerator());

        File file1 = fileService.getResource("/sitemap_index.xml");
        File file2 = fileService.getResource("/sitemap1.xml");
        File file3 = fileService.getResource("/sitemap2.xml");

        compareFiles(file1, "src/test/resources/com/ultracommerce/sitemap/custom/sitemap_index.xml");
        compareFiles(file2, "src/test/resources/com/ultracommerce/sitemap/custom/sitemap1.xml");
        compareFiles(file3, "src/test/resources/com/ultracommerce/sitemap/custom/sitemap2.xml");

    }
    
    @Test
    public void testSiteMapsWithSiteContext() throws SiteMapException, IOException {
        UltraRequestContext brc = new UltraRequestContext();
        UltraRequestContext.setUltraRequestContext(brc);

        Site site = new SiteImpl();
        site.setId(256L);
        brc.setSite(site);
        
        CustomUrlSiteMapGeneratorConfiguration smgc = getConfiguration();
        testGenerator(smgc, new CustomUrlSiteMapGenerator());

        File file1 = fileService.getResource("/sitemap_index.xml");
        File file2 = fileService.getResource("/sitemap1.xml");
        File file3 = fileService.getResource("/sitemap2.xml");
        
        assertThat(file1.getAbsolutePath(), containsString("site-256"));
        assertThat(file2.getAbsolutePath(), containsString("site-256"));
        assertThat(file3.getAbsolutePath(), containsString("site-256"));

        compareFiles(file1, "src/test/resources/com/ultracommerce/sitemap/custom/sitemap_index.xml");
        compareFiles(file2, "src/test/resources/com/ultracommerce/sitemap/custom/sitemap1.xml");
        compareFiles(file3, "src/test/resources/com/ultracommerce/sitemap/custom/sitemap2.xml");
        
        // Remove the request context from thread local so it doesn't get in the way of subsequent tests
        UltraRequestContext.setUltraRequestContext(null);
    }
    
    public CustomUrlSiteMapGeneratorConfiguration getConfiguration() {
        SiteMapUrlEntry urlEntry1 = new SiteMapUrlEntryImpl();
        urlEntry1.setLastMod(new Date());
        urlEntry1.setLocation("http://www.heatclinic.com/1");
        urlEntry1.setSiteMapChangeFreq(SiteMapChangeFreqType.HOURLY);
        urlEntry1.setSiteMapPriority(SiteMapPriorityType.POINT5);

        SiteMapUrlEntry urlEntry2 = new SiteMapUrlEntryImpl();
        urlEntry2.setLastMod(new Date());
        urlEntry2.setLocation("2");
        urlEntry2.setSiteMapChangeFreq(SiteMapChangeFreqType.HOURLY);
        urlEntry2.setSiteMapPriority(SiteMapPriorityType.POINT5);

        SiteMapUrlEntry urlEntry3 = new SiteMapUrlEntryImpl();
        urlEntry3.setLastMod(new Date());
        urlEntry3.setLocation("/3");
        urlEntry3.setSiteMapChangeFreq(SiteMapChangeFreqType.HOURLY);
        urlEntry3.setSiteMapPriority(SiteMapPriorityType.POINT5);

        List<SiteMapUrlEntry> urlEntries = new ArrayList<>();
        urlEntries.add(urlEntry1);
        urlEntries.add(urlEntry2);
        urlEntries.add(urlEntry3);

        CustomUrlSiteMapGeneratorConfiguration smgc = new CustomUrlSiteMapGeneratorConfigurationImpl();
        smgc.setDisabled(false);
        smgc.setSiteMapGeneratorType(SiteMapGeneratorType.CUSTOM);
        smgc.setCustomURLEntries(urlEntries);
        
        return smgc;
    }

}
