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



/**
 * 
 * @author bpolster
 * @author Phillip Verheyden (phillipuniverse)
 */
public class UltraFileUtils {

    /**
     * @deprecated this is now just a pass-through to {@link #appendUnixPaths(String, String)}. The original method was a
     * misnomer.
     */
    @Deprecated
    public static String buildFilePath(String directory, String fileName) {
        return appendUnixPaths(directory, fileName);
    }
    
    /**
     * @deprecated this is now just a pass-through to {@link #removeLeadingUnixSlash(String, String)}. The original method was a
     * misnomer.
     */
    @Deprecated
    public static String removeLeadingSlash(String fileName) {
        return removeLeadingUnixSlash(fileName);
    }
    
    /**
     * @deprecated this is now just a pass-through to {@link #addLeadingUnixSlash(String, String)}. The original method was a
     * misnomer.
     */
    @Deprecated
    public static String addLeadingSlash(String fileName) {
        return addLeadingUnixSlash(fileName);
    }
    
    /**
     * Builds a file path that ensures the directory and filename are separated by a single separator. This is only suitable
     * for Unix and URL paths. File paths need special care for the differences between systems (/ on Unix and \ on Windows)
     * @param directory
     * @param fileName
     */
    public static String appendUnixPaths(String directory, String fileName) {
        if (directory.endsWith("/")) {
            return directory + removeLeadingUnixSlash(fileName);
        } else {
            return directory + addLeadingUnixSlash(fileName);
        }
    }

    /**
     * Removes the leading slash if found on the passed in filename.
     * @param fileName
     */
    public static String removeLeadingUnixSlash(String fileName) {
        if (fileName.startsWith("/")) {
            return fileName.substring(1);
        }
        return fileName;
    }

    /**
     * Adds the leading slash if needed on the beginning of a filename.
     * @param fileName
     */
    public static String addLeadingUnixSlash(String fileName) {
        if (fileName.startsWith("/")) {
            return fileName;
        }
        return "/" + fileName;
    }
}
