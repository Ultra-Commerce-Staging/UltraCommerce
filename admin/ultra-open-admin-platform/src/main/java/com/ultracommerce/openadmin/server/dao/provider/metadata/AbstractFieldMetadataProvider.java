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

import org.apache.commons.lang.StringUtils;
import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.OptionFilterParamType;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.common.presentation.override.AdminPresentationMergeEntry;
import com.ultracommerce.common.util.Tuple;
import com.ultracommerce.openadmin.dto.BasicFieldMetadata;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.override.MetadataOverride;
import com.ultracommerce.openadmin.server.dao.DynamicEntityDao;
import com.ultracommerce.openadmin.server.dao.FieldInfo;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

/**
 * @author Jeff Fischer
 */
public abstract class AbstractFieldMetadataProvider extends AbstractMetadataProvider implements FieldMetadataProvider {

    @Resource(name = "ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;
    
    @Resource(name = "ucUltraEnumerationUtility")
    protected UltraEnumerationUtility enumerationUtility;

    protected void setClassOwnership(Class<?> parentClass, Class<?> targetClass, Map<String, FieldMetadata> attributes, FieldInfo field) {
        FieldMetadata metadata = attributes.get(field.getName());
        if (metadata != null) {
            AdminPresentationClass adminPresentationClass;
            if (parentClass != null) {
                metadata.setOwningClass(parentClass.getName());
                adminPresentationClass = AnnotationUtils.findAnnotation(parentClass, AdminPresentationClass.class);
            } else {
                adminPresentationClass = AnnotationUtils.findAnnotation(targetClass, AdminPresentationClass.class);
            }
            if (adminPresentationClass != null) {
                String friendlyName = adminPresentationClass.friendlyName();
                if (!StringUtils.isEmpty(friendlyName) && StringUtils.isEmpty(metadata.getOwningClassFriendlyName())) {
                    metadata.setOwningClassFriendlyName(friendlyName);
                }
            }
        }
    }

    protected FieldInfo buildFieldInfo(Field field) {
        FieldInfo info = new FieldInfo();
        info.setName(field.getName());
        info.setGenericType(field.getGenericType());
        ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
        if (manyToMany != null) {
            info.setManyToManyMappedBy(manyToMany.mappedBy());
            info.setManyToManyTargetEntity(manyToMany.targetEntity().getName());
        }
        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        if (oneToMany != null) {
            info.setOneToManyMappedBy(oneToMany.mappedBy());
            info.setOneToManyTargetEntity(oneToMany.targetEntity().getName());
        }
        MapKey mapKey = field.getAnnotation(MapKey.class);
        if (mapKey != null) {
            info.setMapKey(mapKey.name());
        }
        return info;
    }

    /**
     * @deprecated use the overloaded method that takes DynamicEntityDao as well. This version does not always properly detect the override from xml.
     * @param configurationKey
     * @param ceilingEntityFullyQualifiedClassname
     * @return override value
     */
    @Deprecated
    protected Map<String, MetadataOverride> getTargetedOverride(String configurationKey, String ceilingEntityFullyQualifiedClassname) {
        if (metadataOverrides != null && (configurationKey != null || ceilingEntityFullyQualifiedClassname != null)) {
            return metadataOverrides.containsKey(configurationKey)?metadataOverrides.get(configurationKey):metadataOverrides.get(ceilingEntityFullyQualifiedClassname);
        }
        return null;
    }

    protected Class<?> getBasicJavaType(SupportedFieldType fieldType) {
        Class<?> response;
        switch (fieldType) {
            case BOOLEAN:
                response = Boolean.TYPE;
                break;
            case DATE:
                response = Date.class;
                break;
            case DECIMAL:
                response = BigDecimal.class;
                break;
            case MONEY:
                response = BigDecimal.class;
                break;
            case INTEGER:
                response = Integer.TYPE;
                break;
            case UNKNOWN:
                response = null;
                break;
            default:
                response = String.class;
                break;
        }

        return response;
    }

    protected Object convertType(String value, OptionFilterParamType type) {
        Object response;
        switch (type) {
            case BIGDECIMAL:
               response = new BigDecimal(value);
               break;
            case BOOLEAN:
               response = Boolean.parseBoolean(value);
               break;
            case DOUBLE:
               response = Double.parseDouble(value);
               break;
            case FLOAT:
               response = Float.parseFloat(value);
               break;
            case INTEGER:
               response = Integer.parseInt(value);
               break;
            case LONG:
               response = Long.parseLong(value);
               break;
            default:
               response = value;
               break;
        }

        return response;
    }

    protected void setupUltraEnumeration(String ultraEnumerationClass, BasicFieldMetadata fieldMetadata, DynamicEntityDao dynamicEntityDao) {
        try {
            List<Tuple<String, String>> enumVals = enumerationUtility.getEnumerationValues(ultraEnumerationClass, dynamicEntityDao);
            
            String[][] enumerationValues = new String[enumVals.size()][2];
            int j = 0;
            for (Tuple<String, String> t : enumVals) {
                enumerationValues[j][0] = t.getFirst();
                enumerationValues[j][1] = t.getSecond();
                j++;
            }
            
            fieldMetadata.setEnumerationValues(enumerationValues);
            fieldMetadata.setEnumerationClass(ultraEnumerationClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Map<String, AdminPresentationMergeEntry> getAdminPresentationEntries(AdminPresentationMergeEntry[] entries) {
        Map<String, AdminPresentationMergeEntry> response = new HashMap<>();
        for (AdminPresentationMergeEntry entry : entries) {
            response.put(entry.propertyType(), entry);
        }
        return response;
    }
}
