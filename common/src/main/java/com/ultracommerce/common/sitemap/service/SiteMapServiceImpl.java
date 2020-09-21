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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.config.domain.ModuleConfiguration;
import com.ultracommerce.common.config.service.ModuleConfigurationService;
import com.ultracommerce.common.config.service.type.ModuleConfigurationType;
import com.ultracommerce.common.file.domain.FileWorkArea;
import com.ultracommerce.common.file.service.UltraFileService;
import com.ultracommerce.common.sitemap.domain.SiteMapConfiguration;
import com.ultracommerce.common.sitemap.domain.SiteMapGeneratorConfiguration;
import com.ultracommerce.common.sitemap.exception.SiteMapException;
import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.common.web.BaseUrlResolver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Resource;

/**
 * Component responsible for generating a sitemap.   Relies on SiteMapGenerators to 
 * produce the actual url entries within the sitemap.
 * 
 * Create a sitemap index file and at least one sitemap file with the URL elements.
 * 
 * @author bpolster
 *
 */
@Service("ucSiteMapService")
public class SiteMapServiceImpl implements SiteMapService {

    protected static final Log LOG = LogFactory.getLog(SiteMapServiceImpl.class);
    
    protected static final String ENCODING_EXTENSION = ".gz";

    protected Boolean gzipSiteMapFiles;

    @Resource(name = "ucModuleConfigurationService")
    protected ModuleConfigurationService moduleConfigurationService;

    @Resource(name = "ucSiteMapGenerators")
    protected List<SiteMapGenerator> siteMapGenerators = new ArrayList<SiteMapGenerator>();

    @Resource(name = "ucFileService")
    protected UltraFileService ultraFileService;

    @Resource(name = "ucBaseUrlResolver")
    protected BaseUrlResolver baseUrlResolver;

    @Override
    public SiteMapGenerationResponse generateSiteMap() throws SiteMapException, IOException {
        SiteMapGenerationResponse smgr = new SiteMapGenerationResponse();
        SiteMapConfiguration smc = findActiveSiteMapConfiguration();
        if (smc == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("No SiteMap generated since no active configuration was found.");
            }
            smgr.setHasError(true);
            smgr.setErrorCode("No SiteMap Configuration Found");
            return smgr;
        }

        FileWorkArea fileWorkArea = ultraFileService.initializeWorkArea();
        SiteMapBuilder siteMapBuilder = new SiteMapBuilder(smc, fileWorkArea, baseUrlResolver.getSiteBaseUrl(), getGzipSiteMapFiles());

        if (LOG.isTraceEnabled()) {
            LOG.trace("File work area initalized with path " + fileWorkArea.getFilePathLocation());
        }
        for (SiteMapGeneratorConfiguration currentConfiguration : smc.getSiteMapGeneratorConfigurations()) {
            if (currentConfiguration.isDisabled()) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Skipping disabled sitemap generator configuration" + currentConfiguration.getId());
                }
                continue;
            }
            SiteMapGenerator generator = selectSiteMapGenerator(currentConfiguration);
            if (generator != null) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("SiteMapGenerator found, adding entries" + generator.getClass());
                }
                generator.addSiteMapEntries(currentConfiguration, siteMapBuilder);
            } else {
                LOG.warn("No site map generator found to process generator configuration for " + currentConfiguration.getSiteMapGeneratorType());
            }
        }

        siteMapBuilder.persistSiteMap();


        // Check for GZip
        if (getGzipSiteMapFiles()) {
            gzipAndDeleteFiles(fileWorkArea, siteMapBuilder.getIndexedFileNames(), false);
            List<String> indexFileNames = new ArrayList<String>();
            for (String fileName: siteMapBuilder.getIndexedFileNames()) {
                indexFileNames.add(fileName + ENCODING_EXTENSION);
            }
            smgr.setSiteMapFilePaths(indexFileNames);
        } else {
            smgr.setSiteMapFilePaths(siteMapBuilder.getIndexedFileNames());
        }


        // Move the generated files to their permanent location
        ultraFileService.addOrUpdateResources(fileWorkArea, true);
        ultraFileService.closeWorkArea(fileWorkArea);

        return smgr;
    }

    @Override
    public File getSiteMapFile(String fileName) throws SiteMapException, IOException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Method getSiteMapFile() invoked for " + fileName);
        }
        File siteMapFile = ultraFileService.getResource(fileName, getSiteMapTimeoutInMillis());
        if (siteMapFile.exists()) {

            if (getAutoGenerateSiteMapAfterTimeout()) {
                long lastModified = siteMapFile.lastModified();
                long now = System.currentTimeMillis();
                // Create new SiteMap if timeout expired.
                if ((now - lastModified) > getSiteMapTimeoutInMillis().longValue()) {
                    generateSiteMap();
                    siteMapFile = ultraFileService.getResource(fileName, getSiteMapTimeoutInMillis());
                }
            }
            if (LOG.isTraceEnabled()) {
                LOG.trace("Returning existing SiteMap");
            }
            return siteMapFile;

        } else {
            if (getCreateSiteMapIfNotFound()) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Generating SiteMap");
                }
                generateSiteMap();
                siteMapFile = ultraFileService.getResource(fileName, getSiteMapTimeoutInMillis());
                if (siteMapFile.exists()) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Returning SiteMap file " + fileName);
                    }
                } else {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Sitemap file " + fileName + " not found after call to generate siteMap.xml");
                    }
                }
                return siteMapFile;
            } else {
                return null;
            }
        }        
    }

    protected SiteMapConfiguration findActiveSiteMapConfiguration() {
        List<ModuleConfiguration> configurations = moduleConfigurationService.findActiveConfigurationsByType(ModuleConfigurationType.SITE_MAP);

        SiteMapConfiguration smc = null;
        if (configurations != null && !configurations.isEmpty()) {
            //Try to find a default configuration           
            for (ModuleConfiguration configuration : configurations) {
                if (configuration.getIsDefault()) {
                    smc = (SiteMapConfiguration) configuration;
                    break;
                }
            }
            if (smc == null) {
                //if there wasn't a default one, use the first active one...
                smc = (SiteMapConfiguration) configurations.get(0);
            }
        }

        return smc;
    }

    /**
     * Returns the siteMapGenerator most qualified to handle the given configuration.     
     * 
     * @param smgc
     * @return
     */
    protected SiteMapGenerator selectSiteMapGenerator(SiteMapGeneratorConfiguration smgc) {
        for (SiteMapGenerator siteMapGenerator : siteMapGenerators) {
            if (siteMapGenerator.canHandleSiteMapConfiguration(smgc)) {
                return siteMapGenerator;
            }
        }
        return null;
    }

    /**
     *
     * @param fileWorkArea
     * @param fileNames
     */
    protected void gzipAndDeleteFiles(FileWorkArea fileWorkArea, List<String> fileNames,boolean shouldDeleteOriginal){
        for (String fileName : fileNames) {
            try {
                String fileNameWithPath = FilenameUtils.normalize(fileWorkArea.getFilePathLocation() + File.separator + fileName);

                FileInputStream fis = new FileInputStream(fileNameWithPath);
                FileOutputStream fos = new FileOutputStream(fileNameWithPath + ENCODING_EXTENSION);
                GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    gzipOS.write(buffer, 0, len);
                }
                //close resources
                gzipOS.close();
                fos.close();
                fis.close();

                if(shouldDeleteOriginal){
                    File originalFile = new File(fileNameWithPath);
                    originalFile.delete();
                }


            } catch (IOException e) {
                LOG.error("Error writing zip file.", e);
            }
        }
    }

    /**
     * GZip a file, Then delete it
     * @param fileWorkArea
     * @param fileNames
     */
    protected void gzipAndDeleteFiles(FileWorkArea fileWorkArea, List<String> fileNames) {
        gzipAndDeleteFiles(fileWorkArea,fileNames,true);
    }


    public List<SiteMapGenerator> getSiteMapGenerators() {
        return siteMapGenerators;
    }

    public void setSiteMapGenerators(List<SiteMapGenerator> siteMapGenerators) {
        this.siteMapGenerators = siteMapGenerators;
    }

    public ModuleConfigurationService getModuleConfigurationService() {
        return moduleConfigurationService;
    }

    public void setModuleConfigurationService(ModuleConfigurationService moduleConfigurationService) {
        this.moduleConfigurationService = moduleConfigurationService;
    }

    protected boolean getGzipSiteMapFilesDefault() {
        return UCSystemProperty.resolveBooleanSystemProperty("sitemap.gzip.files");
    }

    public boolean getCreateSiteMapIfNotFound() {
        return UCSystemProperty.resolveBooleanSystemProperty("sitemap.createIfNotFound");
    }

    public boolean getAutoGenerateSiteMapAfterTimeout() {
        return UCSystemProperty.resolveBooleanSystemProperty("sitemap.createIfTimeoutExpired",false);
    }

    public Long getSiteMapTimeoutInMillis() {
        Long cacheSeconds = UCSystemProperty.resolveLongSystemProperty("sitemap.cache.seconds");
        return cacheSeconds * 1000;
    }


    public void setGzipSiteMapFiles(Boolean gzipSiteMapFiles) {
        this.gzipSiteMapFiles = gzipSiteMapFiles;
    }

    public boolean getGzipSiteMapFiles() {
        if (this.gzipSiteMapFiles != null) {
            return this.gzipSiteMapFiles.booleanValue();
        } else {
            return getGzipSiteMapFilesDefault();
        }
    }
}
