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
package com.ultracommerce.openadmin.web.service;

import com.ultracommerce.common.media.domain.Media;

/**
 * @author Chad Harchar (charchar)
 */
public interface MediaBuilderService {

    /**
     * Converts the given json {@link String} to {@link com.ultracommerce.common.media.domain.Media} given the
     * {@link java.lang.Class} that has been passed in.
     *
     * @param json the {@link String} to be converted to {@link Media}
     * @param type the {@link Class} that the {@link Media} should be
     */
    public Media convertJsonToMedia(String json, Class<?> type);

    /**
     * Instantiates any {@link com.ultracommerce.common.media.domain.Media} assignable field on the passed in
     * {@link com.ultracommerce.common.media.domain.Media} object.  This is used for xref
     * objects that implement {@link com.ultracommerce.common.media.domain.Media}
     *
     * @param media
     */
    public void instantiateMediaFields(Media media);
}
