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
package com.ultracommerce.admin.server.provider.metadata;

import com.ultracommerce.common.presentation.client.VisibilityEnum;
import com.ultracommerce.openadmin.dto.BasicFieldMetadata;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.server.dao.provider.metadata.AbstractFieldMetadataProvider;
import com.ultracommerce.openadmin.server.dao.provider.metadata.FieldMetadataProvider;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.AddFieldMetadataRequest;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.AddMetadataFromFieldTypeRequest;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.AddMetadataFromMappingDataRequest;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.LateStageAddMetadataRequest;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.OverrideViaAnnotationRequest;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.OverrideViaXmlRequest;
import com.ultracommerce.openadmin.server.service.type.MetadataProviderResponse;
import com.ultracommerce.profile.core.domain.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * Modifies Username/EmailAddress fields based on the 'use.email.for.site.login' property
 *
 * @author ckittrell
 */
@Component("ucCustomerUsernameFieldMetadataProvider")
@Scope("prototype")
public class CustomerUsernameFieldMetadataProvider extends AbstractFieldMetadataProvider implements FieldMetadataProvider {

    @Value("${use.email.for.site.login:true}")
    protected boolean useEmailForLogin;

    @Override
    public int getOrder() {
        return FieldMetadataProvider.BASIC;
    }

    private boolean canHandleRequest(LateStageAddMetadataRequest addMetadataRequest) {
        return Customer.class.isAssignableFrom(addMetadataRequest.getTargetClass());
    }

    @Override
    public MetadataProviderResponse lateStageAddMetadata(LateStageAddMetadataRequest addMetadataRequest, Map<String, FieldMetadata> metadata) {
        if (!canHandleRequest(addMetadataRequest)) {
            return MetadataProviderResponse.NOT_HANDLED;
        }

        if (useEmailForLogin) {
            BasicFieldMetadata username = (BasicFieldMetadata) metadata.get("username");
            username.setVisibility(VisibilityEnum.HIDDEN_ALL);
            username.setRequiredOverride(false);

            BasicFieldMetadata emailAddress = (BasicFieldMetadata) metadata.get("emailAddress");
            emailAddress.setRequiredOverride(true);
        }

        return MetadataProviderResponse.HANDLED;
    }

    @Override
    public MetadataProviderResponse addMetadataFromFieldType(AddMetadataFromFieldTypeRequest addMetadataFromFieldTypeRequest, Map<String, FieldMetadata> metadata) {
        return MetadataProviderResponse.NOT_HANDLED;
    }

    @Override
    public MetadataProviderResponse addMetadata(AddFieldMetadataRequest addMetadataRequest, Map<String, FieldMetadata> metadata) {
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
