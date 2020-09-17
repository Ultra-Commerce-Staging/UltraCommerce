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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.media.domain.Media;
import com.ultracommerce.common.persistence.EntityConfiguration;
import com.ultracommerce.common.util.StringUtil;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;

import javax.annotation.Resource;

/**
 * @author Chad Harchar (charchar)
 */
@Service("ucMediaBuilderService")
public class MediaBuilderServiceImpl implements MediaBuilderService {

    private static final Log LOG = LogFactory.getLog(MediaBuilderServiceImpl.class);

    @Resource(name="ucEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public Media convertJsonToMedia(String json, Class<?> type) {
        if (json != null && !"".equals(json)) {
            try {
                ObjectMapper om = new ObjectMapper();
                om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return (Media) om.readValue(json, type);
            } catch (Exception e) {
                LOG.warn("Error parsing json to media " + StringUtil.sanitize(json), e);
            }
        }
        return entityConfiguration.createEntityInstance(Media.class.getName(), Media.class);
    }

    @Override
    public void instantiateMediaFields(Media media) {
        Field[] fields = media.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Media.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                try {
                    Media mediaField = entityConfiguration.createEntityInstance(Media.class.getName(), Media.class);
                    field.set(media, mediaField);
                } catch (IllegalAccessException e) {
                    LOG.warn("Error initializing media field " + StringUtil.sanitize(field.getName()) + " on " + media.getClass().getName(), e);
                }
            }
        }
    }
}
