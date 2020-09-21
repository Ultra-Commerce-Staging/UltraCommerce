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

import com.ultracommerce.common.file.domain.FileWorkArea;
import com.ultracommerce.common.file.service.type.FileApplicationType;

import java.io.File;
import java.util.List;

/**
 * Interface to be implemented by a FileProvider.   This could be a local FileProvider or a remote service like Amazon S3.
 * 
 * @author bpolster
 *
 */
public interface FileServiceProvider {
    
    /**
     * Returns a File representing the passed in url. All separators in the given <b>url</b> should be in URL-separator form
     * meaning '/' rather than '\' (like on Windows).
     * 
     * @param name - fully qualified path to the resource
     * @return
     */
    File getResource(String url);

    /**
     * Returns a File representing the passed in name and application type.   Providers may choose to 
     * cache certain FileApplicationType(s) locally rather than retrieve them from a remote source.   
     *  
     * @param url - the URL-representation of the resource. This means that paths should always have / separators rather than
     * system-specific values
     * @param fileApplicationType applicationType
     * @return a File to the 
     */
    File getResource(String url, FileApplicationType fileApplicationType);

    /**
     * Takes in a work area and application type and moves all of the files to the configured FileProvider.
     * 
     * @param workArea
     * @param files the files that should be copied
     * @deprecated use {@link #addOrUpdateResourcesForPaths(FileWorkArea, List, boolean)}
     */
    @Deprecated
    void addOrUpdateResources(FileWorkArea workArea, List<File> files, boolean removeFilesFromWorkArea);

    /**
     * Adds all of the given <b>files</b> and returns the resource names of all of them suitable for invoking
     * {@link #getResource(String)} and/or {@link #removeResource(String)}
     * 
     * @param workArea
     * @param files
     * @param removeFilesFromWorkArea
     * @return the resource names for each file that was uploaded. The resource names that are returned should be for each
     * file in <b>file</b>
     */
    List<String> addOrUpdateResourcesForPaths(FileWorkArea workArea, List<File> files, boolean removeFilesFromWorkArea);
    
    /**
     * Removes the resource from the file service.
     * 
     * @param name - fully qualified path to the resource
     * @return true if the resource was removed
     */
    boolean removeResource(String name);
}
