/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.openadmin.server.service.persistence.module.provider.extension;

import com.ultracommerce.common.extension.ExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.media.domain.Media;
import com.ultracommerce.common.util.Tuple;
import com.ultracommerce.openadmin.server.service.persistence.module.provider.request.PopulateValueRequest;

/**
 * For internal usage. Allows extending API calls without subclassing the entity.
 *
 * @author Jeff Fischer
 */
public interface MediaFieldPersistenceProviderExtensionHandler extends ExtensionHandler {

    ExtensionResultStatusType transformId(Media media, ExtensionResultHolder<Long> resultHolder);

    ExtensionResultStatusType postAdd(Media media);

    ExtensionResultStatusType postUpdate(Media media);

    ExtensionResultStatusType retrieveMedia(Object instance, PopulateValueRequest request, ExtensionResultHolder<Tuple<Media, Boolean>> resultHolder);

    ExtensionResultStatusType checkDirtyState(Media oldMedia, Media newMedia, ExtensionResultHolder<Boolean> resultHolder);

    public static final int DEFAULT_PRIORITY = Integer.MAX_VALUE;

}
