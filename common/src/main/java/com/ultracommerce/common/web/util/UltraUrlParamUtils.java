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
package com.ultracommerce.common.web.util;

import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;

/**
 * @author Chris Kittrell (ckittrell)
 */
public class UltraUrlParamUtils {

    public static String addPaginationParam(String url, String paginationParamName, Integer pageNumber) {
        try {
            if (pageNumber > 1) {
                return new URIBuilder(url).addParameter(paginationParamName, String.valueOf(pageNumber)).build().toString();
            }
        } catch (URISyntaxException e) {
            // If we run into trouble, do nothing - we'll just return the url that we were given.
        }
        return url;
    }

}
