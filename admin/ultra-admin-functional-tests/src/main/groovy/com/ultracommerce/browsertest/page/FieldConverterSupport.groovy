/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.browsertest.page

/**
 * Common class shared between classes that are using an {@link EntityForm}-like representation for showing a form to a
 * user. Another consumer of this is an {@link AdornedTargetPage} which is not <i>really</i> a pure {@link EntityForm} but
 * has similar form capabilities nonetheless (specifically, how fields are resolved on the page)
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
class FieldConverterSupport {

    /**
     * Converts the given field name into its selector representation. For instance, if you pass in 'defaultSku.name' this
     * will give you back input\[name="fields[\\'defaultSku__name\\'\].value"]. This selector can then be used to
     * find the field on the form.
     * @param fieldName a potentially dot-separated field name on the form
     * @return a JQuery selector string
     */
    def String convertFieldName(String fieldName) {
        // not hardcoding input[] to work with textarea as well
        '[name="fields\\[\\\'' + fieldName.replaceAll('\\.', '__') + '\\\'\\].value"]'
    }
    
}
