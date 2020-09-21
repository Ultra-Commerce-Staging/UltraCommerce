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
package com.ultracommerce.openadmin.server.dao.provider.metadata;

import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.openadmin.dto.BasicFieldMetadata;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.AddFieldMetadataRequest;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.AddMetadataFromFieldTypeRequest;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.AddMetadataFromMappingDataRequest;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.LateStageAddMetadataRequest;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.OverrideViaAnnotationRequest;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.OverrideViaXmlRequest;
import com.ultracommerce.openadmin.server.service.type.MetadataProviderResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Adds a new 'passwordConfirm' field to the metadata as well as ensures that the field type for the password field is
 * actually a password
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucPasswordFieldMetadataProvider")
@Scope("prototype")
public class PasswordFieldMetadataProvider extends AbstractFieldMetadataProvider implements FieldMetadataProvider {

    @Override
    public int getOrder() {
        return FieldMetadataProvider.BASIC;
    }

    @Override
    public MetadataProviderResponse addMetadataFromFieldType(AddMetadataFromFieldTypeRequest addMetadataFromFieldTypeRequest, Map<String, FieldMetadata> metadata) {
        if (addMetadataFromFieldTypeRequest.getPresentationAttribute() instanceof BasicFieldMetadata &&
                SupportedFieldType.PASSWORD.equals(((BasicFieldMetadata) addMetadataFromFieldTypeRequest.getPresentationAttribute()).getExplicitFieldType())) {
            //build the metadata for the password field
            addMetadataFromFieldTypeRequest.getDynamicEntityDao().getDefaultFieldMetadataProvider().addMetadataFromFieldType(addMetadataFromFieldTypeRequest, metadata);
            ((BasicFieldMetadata) addMetadataFromFieldTypeRequest.getPresentationAttribute()).setFieldType(SupportedFieldType.PASSWORD);

            BasicFieldMetadata originalMd = (BasicFieldMetadata) addMetadataFromFieldTypeRequest.getPresentationAttribute();

            //clone the password field and add in a custom one
            BasicFieldMetadata confirmMd = (BasicFieldMetadata) originalMd.cloneFieldMetadata();
            confirmMd.setFieldName("passwordConfirm");
            confirmMd.setFriendlyName("AdminUserImpl_Admin_Password_Confirm");
            confirmMd.setExplicitFieldType(SupportedFieldType.PASSWORD_CONFIRM);
            confirmMd.setValidationConfigurations(new HashMap<String, List<Map<String,String>>>());
            confirmMd.setOrder(originalMd.getOrder() + 1);
            metadata.put("passwordConfirm", confirmMd);
            return MetadataProviderResponse.HANDLED;
        } else {
            return MetadataProviderResponse.NOT_HANDLED;
        }
    }

    @Override
    public MetadataProviderResponse addMetadata(AddFieldMetadataRequest addMetadataRequest, Map<String, FieldMetadata> metadata) {
        return MetadataProviderResponse.NOT_HANDLED;
    }

    @Override
    public MetadataProviderResponse lateStageAddMetadata(LateStageAddMetadataRequest addMetadataRequest, Map<String, FieldMetadata> metadata) {
        return MetadataProviderResponse.NOT_HANDLED;
    }

    @Override
    public MetadataProviderResponse overrideViaAnnotation(OverrideViaAnnotationRequest overrideViaAnnotationRequest, Map<String, FieldMetadata> metadata) {
        return MetadataProviderResponse.NOT_HANDLED;
    }

    @Override
    public MetadataProviderResponse overrideViaXml(OverrideViaXmlRequest overrideViaXmlRequest, Map<String, FieldMetadata> metadata) {
        return MetadataProviderResponse.NOT_HANDLED;
    }

    @Override
    public MetadataProviderResponse addMetadataFromMappingData(AddMetadataFromMappingDataRequest addMetadataFromMappingDataRequest, FieldMetadata metadata) {
        return MetadataProviderResponse.NOT_HANDLED;
    }

}
