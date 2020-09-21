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
package com.ultracommerce.openadmin.server.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.PopulateToOneFieldsEnum;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.openadmin.dto.ClassMetadata;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.MergedPropertyType;
import com.ultracommerce.openadmin.dto.TabMetadata;
import com.ultracommerce.openadmin.server.dao.provider.metadata.BasicEntityMetadataProvider;
import com.ultracommerce.openadmin.server.dao.provider.metadata.DefaultFieldMetadataProvider;
import com.ultracommerce.openadmin.server.dao.provider.metadata.FieldMetadataProvider;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.AddFieldMetadataRequest;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.AddMetadataFromMappingDataRequest;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.AddMetadataRequest;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.OverrideViaAnnotationRequest;
import com.ultracommerce.openadmin.server.dao.provider.metadata.request.OverrideViaXmlRequest;
import com.ultracommerce.openadmin.server.service.type.MetadataProviderResponse;
import org.hibernate.mapping.Property;
import org.hibernate.type.Type;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @author Jeff Fischer
 */
@Component("ucMetadata")
@Scope("prototype")
public class Metadata {

    private static final Log LOG = LogFactory.getLog(Metadata.class);

    @Resource(name="ucFieldMetadataProviders")
    protected List<FieldMetadataProvider> fieldMetadataProviders = new ArrayList<>();

    @Resource(name= "ucDefaultFieldMetadataProvider")
    protected FieldMetadataProvider defaultFieldMetadataProvider;

    @Resource(name= "ucBasicEntityMetadataProvider")
    protected BasicEntityMetadataProvider basicEntityMetadataProvider;

    public Map<String, FieldMetadata> getFieldMetadataForTargetClass(Class<?> parentClass, Class<?> targetClass, DynamicEntityDao dynamicEntityDao, String prefix) {
        Map<String, FieldMetadata> metadata = new HashMap<>();
        Field[] fields = dynamicEntityDao.getAllFields(targetClass);
        for (Field field : fields) {
            boolean foundOneOrMoreHandlers = false;
            for (FieldMetadataProvider fieldMetadataProvider : fieldMetadataProviders) {
                MetadataProviderResponse response = fieldMetadataProvider.addMetadata(new AddFieldMetadataRequest(field, parentClass, targetClass,
                        dynamicEntityDao, prefix), metadata);
                if (MetadataProviderResponse.NOT_HANDLED != response) {
                    foundOneOrMoreHandlers = true;
                }
                if (MetadataProviderResponse.HANDLED_BREAK == response) {
                    break;
                }
            }
            if (!foundOneOrMoreHandlers) {
                defaultFieldMetadataProvider.addMetadata(new AddFieldMetadataRequest(field, parentClass, targetClass,
                        dynamicEntityDao, prefix), metadata);
            }
        }
        return metadata;
    }

    public Map<String, TabMetadata> getBaseTabAndGroupMetadata(Class<?>[] entities) {
        Map<String, TabMetadata> baseTabAndGroupMetadata = new HashMap<>();

        // Go in reverse order since we want the lowest subclass to come last to guarantee that it takes effect
        for (int i = entities.length-1;i >= 0; i--) {
            basicEntityMetadataProvider.addTabAndGroupMetadata(new AddMetadataRequest(null, entities[i], null, ""),
                baseTabAndGroupMetadata);
        }

        return baseTabAndGroupMetadata;
    }

    public void applyTabAndGroupMetadataOverrides(Class<?>[] entities, Map<String, TabMetadata> mergedTabAndGroupMetadata) {
        // Go in reverse order since we want the lowest subclass to come last to guarantee that it takes effect
        for (int i = entities.length-1;i >= 0; i--) {
            basicEntityMetadataProvider.overrideMetadataViaAnnotation(new OverrideViaAnnotationRequest(entities[i], true, null, ""),
                    mergedTabAndGroupMetadata);
            basicEntityMetadataProvider.overrideMetadataViaXml(new OverrideViaXmlRequest("", entities[i].getCanonicalName(), "", true, null),
                    mergedTabAndGroupMetadata);
        }
    }

    public void buildAdditionalTabAndGroupMetadataFromCmdProperties(ClassMetadata cmd, Map<String, TabMetadata> metadata) {
        basicEntityMetadataProvider.addTabAndGroupMetadataFromCmdProperties(cmd, metadata);
    }

    public Map<String, FieldMetadata> overrideMetadata(Class<?>[] entities, PropertyBuilder propertyBuilder, String prefix, Boolean isParentExcluded, String ceilingEntityFullyQualifiedClassname, String configurationKey, DynamicEntityDao dynamicEntityDao) {
        Boolean classAnnotatedPopulateManyToOneFields = null;
        //go in reverse order since I want the lowest subclass override to come last to guarantee that it takes effect
        for (int i = entities.length-1;i >= 0; i--) {
            AdminPresentationClass adminPresentationClass = AnnotationUtils.findAnnotation(entities[i], AdminPresentationClass.class);
            if (adminPresentationClass != null && adminPresentationClass.populateToOneFields() != PopulateToOneFieldsEnum.NOT_SPECIFIED) {
                classAnnotatedPopulateManyToOneFields = adminPresentationClass.populateToOneFields()==PopulateToOneFieldsEnum.TRUE;
                break;
            }
        }

        Map<String, FieldMetadata> mergedProperties = propertyBuilder.execute(classAnnotatedPopulateManyToOneFields);
        for (int i = entities.length-1;i >= 0; i--) {
            boolean handled = false;
            for (FieldMetadataProvider fieldMetadataProvider : fieldMetadataProviders) {
                MetadataProviderResponse response = fieldMetadataProvider.overrideViaAnnotation(new OverrideViaAnnotationRequest(entities[i],
                            isParentExcluded, dynamicEntityDao, prefix), mergedProperties);
                if (MetadataProviderResponse.NOT_HANDLED != response) {
                    handled = true;
                }
                if (MetadataProviderResponse.HANDLED_BREAK == response) {
                    break;
                }
            }
            if (!handled) {
                defaultFieldMetadataProvider.overrideViaAnnotation(new OverrideViaAnnotationRequest(entities[i],
                                         isParentExcluded, dynamicEntityDao, prefix), mergedProperties);
            }
        }
        ((DefaultFieldMetadataProvider) defaultFieldMetadataProvider).overrideExclusionsFromXml(new OverrideViaXmlRequest(configurationKey,
                ceilingEntityFullyQualifiedClassname, prefix, isParentExcluded, dynamicEntityDao), mergedProperties);

        boolean handled = false;
        for (FieldMetadataProvider fieldMetadataProvider : fieldMetadataProviders) {
            MetadataProviderResponse response = fieldMetadataProvider.overrideViaXml(
                    new OverrideViaXmlRequest(configurationKey, ceilingEntityFullyQualifiedClassname, prefix,
                            isParentExcluded, dynamicEntityDao), mergedProperties);
            if (MetadataProviderResponse.NOT_HANDLED != response) {
                handled = true;
            }
            if (MetadataProviderResponse.HANDLED_BREAK == response) {
                break;
            }
        }
        if (!handled) {
            defaultFieldMetadataProvider.overrideViaXml(
                                new OverrideViaXmlRequest(configurationKey, ceilingEntityFullyQualifiedClassname, prefix,
                                        isParentExcluded, dynamicEntityDao), mergedProperties);
        }

        return mergedProperties;
    }

    public FieldMetadata getFieldMetadata(
        String prefix,
        String propertyName,
        List<Property> componentProperties,
        SupportedFieldType type,
        Type entityType,
        Class<?> targetClass,
        FieldMetadata presentationAttribute,
        MergedPropertyType mergedPropertyType,
        DynamicEntityDao dynamicEntityDao
    ) {
        return getFieldMetadata(prefix, propertyName, componentProperties, type, null, entityType, targetClass, presentationAttribute, mergedPropertyType, dynamicEntityDao);
    }

    public FieldMetadata getFieldMetadata(
        String prefix,
        final String propertyName,
        final List<Property> componentProperties,
        final SupportedFieldType type,
        final SupportedFieldType secondaryType,
        final Type entityType,
        Class<?> targetClass,
        final FieldMetadata presentationAttribute,
        final MergedPropertyType mergedPropertyType,
        final DynamicEntityDao dynamicEntityDao
    ) {
        if (presentationAttribute.getTargetClass() == null) {
            presentationAttribute.setTargetClass(targetClass.getName());
            presentationAttribute.setFieldName(propertyName);
        }
        presentationAttribute.setInheritedFromType(targetClass.getName());
        presentationAttribute.setAvailableToTypes(new String[]{targetClass.getName()});
        boolean handled = false;
        for (FieldMetadataProvider fieldMetadataProvider : fieldMetadataProviders) {
            MetadataProviderResponse response = fieldMetadataProvider.addMetadataFromMappingData(new AddMetadataFromMappingDataRequest(
                componentProperties, type, secondaryType, entityType, propertyName, mergedPropertyType, dynamicEntityDao), presentationAttribute);
            if (MetadataProviderResponse.NOT_HANDLED != response) {
                handled = true;
            }
            if (MetadataProviderResponse.HANDLED_BREAK == response) {
                break;
            }
        }
        if (!handled) {
            defaultFieldMetadataProvider.addMetadataFromMappingData(new AddMetadataFromMappingDataRequest(
                    componentProperties, type, secondaryType, entityType, propertyName, mergedPropertyType, dynamicEntityDao), presentationAttribute);
        }

        return presentationAttribute;
    }

    public FieldMetadataProvider getDefaultFieldMetadataProvider() {
        return defaultFieldMetadataProvider;
    }

    public void setDefaultFieldMetadataProvider(FieldMetadataProvider defaultFieldMetadataProvider) {
        this.defaultFieldMetadataProvider = defaultFieldMetadataProvider;
    }

    public List<FieldMetadataProvider> getFieldMetadataProviders() {
        return fieldMetadataProviders;
    }

    public void setFieldMetadataProviders(List<FieldMetadataProvider> fieldMetadataProviders) {
        this.fieldMetadataProviders = fieldMetadataProviders;
    }
}
