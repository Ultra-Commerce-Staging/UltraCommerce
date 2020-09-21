/*
 * #%L
 * UltraCommerce Framework Web
 * %%
 * Copyright (C) 2009 - 2020 Ultra Commerce
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
package com.ultracommerce.core.web.processor;

import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.presentation.dialect.AbstractUltraTagReplacementProcessor;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import com.ultracommerce.presentation.model.UltraTemplateElement;
import com.ultracommerce.presentation.model.UltraTemplateModel;
import com.ultracommerce.presentation.model.UltraTemplateNonVoidElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("ucGoogleTagManagerProcessor")
public class GoogleTagManagerProcessor extends AbstractUltraTagReplacementProcessor {

    @Autowired
    Environment env;

    @Override
    public String getName() {
        return "google_tag_manager";
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public UltraTemplateModel getReplacementModel(String tagName, Map<String, String> tagAttributes,
                                                      UltraTemplateContext context) {
        if (StringUtils.isBlank(getTagManagerAccountId())) {
            return context.createModel();
        }

        StringBuffer sb = buildDataLayerStart();

        addTagManagerImport(sb);

        return buildScriptTagFromString(context, sb);
    }

    private StringBuffer buildDataLayerStart() {
        StringBuffer sb = new StringBuffer();

        sb.append("dataLayer = [];");

        return sb;
    }

    private StringBuffer addTagManagerImport(StringBuffer sb) {
        sb.append("(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':");
        sb.append("new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],");
        sb.append("j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=");
        sb.append("'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);");
        sb.append("})(window,document,'script','dataLayer','");
        sb.append(getTagManagerAccountId());
        sb.append("');");

        return sb;
    }

    private UltraTemplateModel buildScriptTagFromString(UltraTemplateContext context, StringBuffer sb) {
        UltraTemplateModel model = context.createModel();

        UltraTemplateNonVoidElement scriptTag = context.createNonVoidElement("script");

        UltraTemplateElement script = context.createTextElement(sb.toString());

        scriptTag.addChild(script);
        model.addElement(scriptTag);

        return model;
    }

    private String getTagManagerAccountId() {
        return env.getProperty("googleTagManager.accountId");
    }

}
