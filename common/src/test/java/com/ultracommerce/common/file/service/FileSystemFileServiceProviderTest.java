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
package com.ultracommerce.common.file.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import com.ultracommerce.common.site.domain.Site;
import com.ultracommerce.common.site.domain.SiteImpl;
import com.ultracommerce.common.web.UltraRequestContext;

import java.io.File;

import junit.framework.TestCase;

public class FileSystemFileServiceProviderTest extends TestCase {

    /**
     * For example, if the URL is /product/myproductimage.jpg, then the MD5 would be
     * 35ec52a8dbd8cf3e2c650495001fe55f resulting in the following file on the filesystem
     * {assetFileSystemPath}/64/a7/myproductimage.jpg.
     * 
     * If there is a "siteId" in the UltraRequestContext then the site is also distributed
     * using a similar algorithm but the system attempts to keep images for sites in their own
     * directory resulting in an extra two folders required to reach any given product.   So, for
     * site with id 125, the system will MD5 "site125" in order to build the URL string.   "site125" has an md5
     * string of "7d905e85b8cb72a0477632be2c342bd6".    
     * 
     * So, in this case with the above product URL in site125, the full URL on the filesystem
     * will be:
     * 
     * {assetFileSystemPath}/7d/site125/64/a7/myproductimage.jpg.
     * @throws Exception
     */
    public void testBuildFileName() throws Exception {
        FileSystemFileServiceProvider provider = new FileSystemFileServiceProvider();
        String tmpdir = FileUtils.getTempDirectoryPath();
        if (!tmpdir.endsWith(File.separator)) {
            tmpdir = tmpdir + File.separator;
        }
        provider.fileSystemBaseDirectory = FilenameUtils.concat(tmpdir, "test");
        provider.maxGeneratedDirectoryDepth = 2;
        File file = provider.getResource("/product/myproductimage.jpg");
        
        String resultPath = tmpdir + StringUtils.join(new String[] {"test", "35", "ec", "myproductimage.jpg"}, File.separator);
        assertEquals(file.getAbsolutePath(), FilenameUtils.normalize(resultPath));

        UltraRequestContext brc = new UltraRequestContext();
        UltraRequestContext.setUltraRequestContext(brc);

        Site site = new SiteImpl();
        site.setId(125L);
        brc.setSite(site);

        // try with site specific directory
        file = provider.getResource("/product/myproductimage.jpg");
        resultPath = tmpdir + StringUtils.join(new String[] {"test", "c8", "site-125", "35", "ec", "myproductimage.jpg"}, File.separator);
        assertEquals(file.getAbsolutePath(), resultPath);

        // try with 3 max generated directories
        provider.maxGeneratedDirectoryDepth = 3;
        file = provider.getResource("/product/myproductimage.jpg");
        resultPath = tmpdir + StringUtils.join(new String[] {"test", "c8", "site-125", "35", "ec", "52", "myproductimage.jpg"}, File.separator);
        assertEquals(file.getAbsolutePath(), resultPath);
        
        // Remove the request context from thread local so it doesn't get in the way of subsequent tests
        UltraRequestContext.setUltraRequestContext(null);
    }
    
}
