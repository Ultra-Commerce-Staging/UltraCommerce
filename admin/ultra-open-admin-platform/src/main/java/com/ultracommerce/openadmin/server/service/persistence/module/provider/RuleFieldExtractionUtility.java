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

package com.ultracommerce.openadmin.server.service.persistence.module.provider;

import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.Property;
import com.ultracommerce.openadmin.server.service.persistence.validation.RuleFieldValidator;
import com.ultracommerce.openadmin.web.rulebuilder.DataDTODeserializer;
import com.ultracommerce.openadmin.web.rulebuilder.DataDTOToMVELTranslator;
import com.ultracommerce.openadmin.web.rulebuilder.MVELToDataWrapperTranslator;
import com.ultracommerce.openadmin.web.rulebuilder.MVELTranslationException;
import com.ultracommerce.openadmin.web.rulebuilder.dto.DataDTO;
import com.ultracommerce.openadmin.web.rulebuilder.dto.DataWrapper;
import com.ultracommerce.openadmin.web.rulebuilder.service.RuleBuilderFieldServiceFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

import javax.annotation.Resource;

/**
 * Commonality shared between {@link RuleFieldPersistenceProvider} and {@link RuleFieldValidator}
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucRuleFieldExtractionUtility")
public class RuleFieldExtractionUtility {

    @Resource(name = "ucRuleBuilderFieldServiceFactory")
    protected RuleBuilderFieldServiceFactory ruleBuilderFieldServiceFactory;

    /**
     * Takes a JSON string that came from the frontend form submission and deserializes it into its {@link DataWrapper} dto
     * representation so that it can be converted to an MVEL expression
     * @param json
     * @return
     */
    public DataWrapper convertJsonToDataWrapper(String json) {
        ObjectMapper mapper = new ObjectMapper();
        DataDTODeserializer dtoDeserializer = new DataDTODeserializer();
        SimpleModule module = new SimpleModule("DataDTODeserializerModule", new Version(1, 0, 0, null));
        module.addDeserializer(DataDTO.class, dtoDeserializer);
        mapper.registerModule(module);
        if (json == null || "[]".equals(json)) {
            return null;
        }

        try {
            return mapper.readValue(json, DataWrapper.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts the given {@link DataWrapper} into an MVEL expression
     * @param translator 
     * @param entityKey
     * @param fieldService
     * @param dw
     * @return
     * @throws MVELTranslationException
     */
    public String convertSimpleMatchRuleJsonToMvel(DataDTOToMVELTranslator translator, String entityKey,
            String fieldService, DataWrapper dw) throws MVELTranslationException {
        String mvel = null;
        // there can only be one DataDTO for an appliesTo* rule
        if (dw != null && dw.getData().size() == 1) {
            DataDTO dto = dw.getData().get(0);
            mvel = convertDTOToMvelString(translator, entityKey, dto, fieldService);
        }

        return mvel;
    }

    public String convertDTOToMvelString(DataDTOToMVELTranslator translator, String entityKey, DataDTO dto, String fieldService) throws MVELTranslationException {
        return translator.createMVEL(entityKey, dto, ruleBuilderFieldServiceFactory.createInstance(fieldService));
    }

    /**
     * Converts a simple MVEL rule into its JSON representation suitable for adding to an {@link Entity} to pass to the
     * frontend.
     * @param translator
     * @param mapper
     * @param matchRule
     * @param jsonProp
     * @param fieldService
     * @return
     */
    public Property convertSimpleRuleToJson(MVELToDataWrapperTranslator translator, ObjectMapper mapper,
            String matchRule, String jsonProp, String fieldService) {
        Entity[] matchCriteria = new Entity[1];
        Property[] properties = new Property[1];
        Property mvelProperty = new Property();
        mvelProperty.setName("matchRule");
        mvelProperty.setValue(matchRule == null ? "" : matchRule);
        properties[0] = mvelProperty;
        Entity criteria = new Entity();
        criteria.setProperties(properties);
        matchCriteria[0] = criteria;

        String json;
        try {
            DataWrapper orderWrapper = translator.createRuleData(matchCriteria, "matchRule", null, null,
                    ruleBuilderFieldServiceFactory.createInstance(fieldService));
            json = mapper.writeValueAsString(orderWrapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Property p = new Property();
        p.setName(jsonProp);
        p.setValue(json);

        return p;
    }

}
