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
package com.ultracommerce.common.util;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jon Fleschler (jfleschler)
 */
public class UCFileSystemUtils {

    private static final String CLASSPATH = "classpath:";

    public static String getClasspathFileContents(String filePath) {
        String contents = null;

        try {
            InputStream stream = getClasspathFileInputStream(filePath);

            if (stream != null) {
                contents = IOUtils.toString(stream);
            }

            return contents;
        } catch (IOException e) {
            return null;
        }
    }

    public static InputStream getClasspathFileInputStream(String filePath) {
        try {
            if (filePath.startsWith(CLASSPATH)) {
                // remove "classpath:" from the file path as ClassPathResource does not recognize it
                filePath = filePath.substring(CLASSPATH.length());
            }

            org.springframework.core.io.Resource resource = new ClassPathResource(filePath);
            if (resource.exists()) {
                return resource.getInputStream();
            }

            return null;
        } catch (IOException e) {
            return null;
        }
    }
}
