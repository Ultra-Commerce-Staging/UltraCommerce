/*
 * #%L
 * UltraCommerce Admin Module
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
package com.ultracommerce.admin.server.service.persistence.module.provider;

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.domain.SkuImpl;
import com.ultracommerce.openadmin.dto.Property;
import com.ultracommerce.openadmin.server.service.persistence.module.provider.FieldPersistenceProviderAdapter;
import com.ultracommerce.openadmin.server.service.persistence.module.provider.request.ExtractValueRequest;
import com.ultracommerce.openadmin.server.service.type.MetadataProviderResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Scope("prototype")
@Component("ucForeignSkuFieldPersistenceProvider")
public class ForeignSkuFieldPersistenceProvider extends FieldPersistenceProviderAdapter {
    
    @Override
    public MetadataProviderResponse extractValue(ExtractValueRequest extractValueRequest, Property property) {
        if (!canHandleExtraction(extractValueRequest, property)) {
            return MetadataProviderResponse.NOT_HANDLED;
        }
        
        try {
            String val = extractValueRequest.getFieldManager().getFieldValue(extractValueRequest.getRequestedValue(), 
                    extractValueRequest.getMetadata().getForeignKeyProperty()).toString();
            String displayVal = null;
            
            if (!StringUtils.isEmpty(extractValueRequest.getMetadata().getForeignKeyDisplayValueProperty())) {
                String nameProperty = extractValueRequest.getMetadata().getForeignKeyDisplayValueProperty();
                Sku sku = (Sku) extractValueRequest.getRequestedValue();
                displayVal = extractValueRequest.getRecordHelper().getStringValueFromGetter(sku, nameProperty);
            }
            
            extractValueRequest.setDisplayVal(displayVal);
            
            property.setValue(val);
            property.setDisplayValue(displayVal);
        } catch (Exception e) {
            return MetadataProviderResponse.NOT_HANDLED;
        }
        
        return MetadataProviderResponse.HANDLED_BREAK;
    }
    
    protected boolean canHandleExtraction(ExtractValueRequest extractValueRequest, Property property) {
        String fkc = extractValueRequest.getMetadata().getForeignKeyClass();
        String rvc = null;
        if (extractValueRequest.getRequestedValue() != null) {
            rvc = extractValueRequest.getRequestedValue().getClass().getName();
        }
        
        return (SkuImpl.class.getName().equals(fkc) || Sku.class.getName().equals(fkc)) &&
                (SkuImpl.class.getName().equals(rvc) || Sku.class.getName().equals(rvc)) &&
                extractValueRequest.getMetadata().getFieldType().equals(SupportedFieldType.ADDITIONAL_FOREIGN_KEY);
    }

}
