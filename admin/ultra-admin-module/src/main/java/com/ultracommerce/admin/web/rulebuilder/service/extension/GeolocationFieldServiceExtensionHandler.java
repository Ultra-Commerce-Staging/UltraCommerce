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
package com.ultracommerce.admin.web.rulebuilder.service.extension;

import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.presentation.RuleIdentifier;
import com.ultracommerce.common.presentation.RuleOperatorType;
import com.ultracommerce.common.presentation.RuleOptionType;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.openadmin.web.rulebuilder.dto.FieldData;
import com.ultracommerce.openadmin.web.rulebuilder.service.AbstractRuleBuilderFieldServiceExtensionHandler;
import com.ultracommerce.openadmin.web.rulebuilder.service.RuleBuilderFieldServiceExtensionManager;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Jon Fleschler (jfleschler)
 */
@Component("ucGeolocationFieldServiceExtensionHandler")
public class GeolocationFieldServiceExtensionHandler extends AbstractRuleBuilderFieldServiceExtensionHandler {

    public static final String GEOLOCATON_ATTRIBUTE_NAME = "_ucGeolocationAttribute";

    @Resource(name = "ucRuleBuilderFieldServiceExtensionManager")
    protected RuleBuilderFieldServiceExtensionManager extensionManager;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    @Override
    public ExtensionResultStatusType addFields(List<FieldData> fields, String ruleFieldName, String dtoClassName) {

        if (isGeolocationEnabled() && RuleIdentifier.REQUEST.equals(ruleFieldName)) {
            fields.add(new FieldData.Builder()
                    .label("rule_geolocationCountryCode")
                    .name("countryCode")
                    .operators(RuleOperatorType.TEXT_LIST)
                    .options(RuleOptionType.EMPTY_COLLECTION)
                    .type(SupportedFieldType.STRING)
                    .overrideDtoClassName("com.ultracommerce.core.geolocation.GeolocationDTO")
                    .overrideEntityKey(GEOLOCATON_ATTRIBUTE_NAME)
                    .build());

            fields.add(new FieldData.Builder()
                    .label("rule_geolocationCountryName")
                    .name("countryName")
                    .operators(RuleOperatorType.TEXT_LIST)
                    .options(RuleOptionType.EMPTY_COLLECTION)
                    .type(SupportedFieldType.STRING)
                    .overrideDtoClassName("com.ultracommerce.core.geolocation.GeolocationDTO")
                    .overrideEntityKey(GEOLOCATON_ATTRIBUTE_NAME)
                    .build());

            fields.add(new FieldData.Builder()
                    .label("rule_geolocationRegionCode")
                    .name("regionCode")
                    .operators(RuleOperatorType.TEXT_LIST)
                    .options(RuleOptionType.EMPTY_COLLECTION)
                    .type(SupportedFieldType.STRING)
                    .overrideDtoClassName("com.ultracommerce.core.geolocation.GeolocationDTO")
                    .overrideEntityKey(GEOLOCATON_ATTRIBUTE_NAME)
                    .build());

            fields.add(new FieldData.Builder()
                    .label("rule_geolocationRegionName")
                    .name("regionName")
                    .operators(RuleOperatorType.TEXT_LIST)
                    .options(RuleOptionType.EMPTY_COLLECTION)
                    .type(SupportedFieldType.STRING)
                    .overrideDtoClassName("com.ultracommerce.core.geolocation.GeolocationDTO")
                    .overrideEntityKey(GEOLOCATON_ATTRIBUTE_NAME)
                    .build());

            fields.add(new FieldData.Builder()
                    .label("rule_geolocationCity")
                    .name("city")
                    .operators(RuleOperatorType.TEXT_LIST)
                    .options(RuleOptionType.EMPTY_COLLECTION)
                    .type(SupportedFieldType.STRING)
                    .overrideDtoClassName("com.ultracommerce.core.geolocation.GeolocationDTO")
                    .overrideEntityKey(GEOLOCATON_ATTRIBUTE_NAME)
                    .build());

            fields.add(new FieldData.Builder()
                    .label("rule_geolocationPostalCode")
                    .name("postalCode")
                    .operators(RuleOperatorType.TEXT_LIST)
                    .options(RuleOptionType.EMPTY_COLLECTION)
                    .type(SupportedFieldType.STRING)
                    .overrideDtoClassName("com.ultracommerce.core.geolocation.GeolocationDTO")
                    .overrideEntityKey(GEOLOCATON_ATTRIBUTE_NAME)
                    .build());

            return ExtensionResultStatusType.HANDLED_CONTINUE;
        }

        return ExtensionResultStatusType.NOT_HANDLED;
    }

    protected boolean isGeolocationEnabled() {
        return UCSystemProperty.resolveBooleanSystemProperty("geolocation.api.enabled", false);
    }
}
