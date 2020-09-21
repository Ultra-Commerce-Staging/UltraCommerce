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

@Component("ucGoogleEndTagManagerProcessor")
public class GoogleEndTagManagerProcessor extends AbstractUltraTagReplacementProcessor {

    @Autowired
    Environment env;

    @Override
    public String getName() {
        return "google_end_tag_manager";
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public UltraTemplateModel getReplacementModel(String tagName, Map<String, String> tagAttributes, UltraTemplateContext context) {
        if (StringUtils.isBlank(getTagManagerAccountId())) {
            return context.createModel();
        }

        StringBuffer sb = new StringBuffer();

        addTagManagerImport(sb);

        return buildScriptTagFromString(context, sb);
    }

    private UltraTemplateModel buildScriptTagFromString(UltraTemplateContext context, StringBuffer sb) {
        UltraTemplateModel model = context.createModel();

        UltraTemplateNonVoidElement scriptTag = context.createNonVoidElement("noscript");

        UltraTemplateElement script = context.createTextElement(sb.toString());

        scriptTag.addChild(script);
        model.addElement(scriptTag);

        return model;
    }

    private StringBuffer addTagManagerImport(StringBuffer sb) {
        sb.append("<iframe src=\"https://www.googletagmanager.com/ns.html?id=");
        sb.append(getTagManagerAccountId());
        sb.append("\" height=\"0\" width=\"0\" style=\"display:none;visibility:hidden\"></iframe>");

        return sb;
    }

    private String getTagManagerAccountId() {
        return env.getProperty("googleTagManager.accountId");
    }
}
