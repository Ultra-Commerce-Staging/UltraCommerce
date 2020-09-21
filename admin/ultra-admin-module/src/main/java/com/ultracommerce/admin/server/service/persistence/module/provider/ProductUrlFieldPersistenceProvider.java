/*
 * #%L
 * UltraCommerce Admin Module
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.admin.server.service.persistence.module.provider;

import com.ultracommerce.admin.server.service.persistence.module.provider.extension.ProductUrlFieldPersistenceProviderExtensionManager;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.catalog.domain.ProductImpl;
import com.ultracommerce.openadmin.server.service.persistence.module.provider.FieldPersistenceProviderAdapter;
import com.ultracommerce.openadmin.server.service.persistence.module.provider.request.PopulateValueRequest;
import com.ultracommerce.openadmin.server.service.type.MetadataProviderResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

@Component("ucProductUrlFieldPersistenceProvider")
@Scope("prototype")
public class ProductUrlFieldPersistenceProvider extends FieldPersistenceProviderAdapter {

    @Resource(name = "ucProductUrlFieldPersistenceProviderExtensionManager")
    private ProductUrlFieldPersistenceProviderExtensionManager extensionManager;

    @Override
    public MetadataProviderResponse populateValue(PopulateValueRequest request, Serializable instance) {
        String propName = request.getProperty().getName();
        String val = request.getRequestedValue();

        if ("url".equals(propName) && ProductImpl.class.isAssignableFrom(instance.getClass())) {
            Product product = (Product) instance;

            ExtensionResultHolder<String> holder = new ExtensionResultHolder<>();
            ExtensionResultStatusType result = extensionManager.getProxy().modifyUrl(val, product, holder);

            if (ExtensionResultStatusType.HANDLED == result) {
                product.setUrl(holder.getResult());
                return MetadataProviderResponse.HANDLED;
            }

        }
        return super.populateValue(request, instance);
    }
}
