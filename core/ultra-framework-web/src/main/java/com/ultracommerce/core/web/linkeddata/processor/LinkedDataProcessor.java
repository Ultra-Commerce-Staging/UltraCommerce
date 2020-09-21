/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.linkeddata.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.core.web.linkeddata.generator.LinkedDataGenerator;
import com.ultracommerce.presentation.dialect.AbstractUltraTagReplacementProcessor;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import com.ultracommerce.presentation.model.UltraTemplateElement;
import com.ultracommerce.presentation.model.UltraTemplateModel;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * This processor replaces linkedData tags with metadata for search engine optimization. The
 * data is formatted to Schema.org and Google standards.
 *
 * @author Jacob Mitash
 * @author Nathan Moore (nathanmoore).
 */
@Component("ucLinkedDataProcessor")
public class LinkedDataProcessor extends AbstractUltraTagReplacementProcessor {
    private final Log LOG = LogFactory.getLog(LinkedDataProcessor.class);

    @Resource(name = "ucLinkedDataGenerators")
    protected List<LinkedDataGenerator> linkedDataGenerators;

    @Override
    public UltraTemplateModel getReplacementModel(final String s, final Map<String, String> map, 
                                                      final UltraTemplateContext context) {
        String linkedDataText = "<script type=\"application/ld+json\">\n" +
                                    getData(context.getRequest()) +
                                "\n</script>";

        final UltraTemplateModel model = context.createModel();
        final UltraTemplateElement linkedData = context.createTextElement(linkedDataText);
        model.addElement(linkedData);

        return model;
    }

    /**
     * Get the metadata for the specific page
     * @param request the user request
     * @return the JSON string representation of the linked data
     */
    protected String getData(final HttpServletRequest request) {
        final String requestUrl = request.getRequestURL().toString();
        final JSONArray schemaObjects = new JSONArray();

        for (final LinkedDataGenerator linkedDataGenerator : linkedDataGenerators) {
            if (linkedDataGenerator.canHandle(request)) {
                try {
                    linkedDataGenerator.getLinkedDataJSON(requestUrl, request, schemaObjects);
                } catch(final JSONException e){
                    // the only reason for this exception to be thrown is a null key being put on a JSONObject, 
                    // which shouldn't ever be expected to actually happen
                    LOG.error("A JSON exception occurred while generating LinkedData", e);
                }
            }
        }
        
        return schemaObjects.toString();
    }

    @Override
    public String getName() {
        return "linkedData";
    }
}
