/*
 * #%L
 * UltraCommerce CMS Module
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

package com.ultracommerce.cms.web.processor;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.cms.file.service.StaticAssetService;
import com.ultracommerce.cms.structure.domain.StructuredContentType;
import com.ultracommerce.cms.structure.service.StructuredContentService;
import com.ultracommerce.cms.web.deeplink.ContentDeepLinkServiceImpl;
import com.ultracommerce.common.RequestDTO;
import com.ultracommerce.common.TimeDTO;
import com.ultracommerce.common.locale.domain.Locale;
import com.ultracommerce.common.sandbox.domain.SandBox;
import com.ultracommerce.common.structure.dto.StructuredContentDTO;
import com.ultracommerce.common.time.SystemTime;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.common.web.deeplink.DeepLink;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraVariableModifierProcessor;
import com.ultracommerce.presentation.model.UltraAssignation;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Processor used to display structured content that is maintained with the Ultra CMS.
 *
 * Usage based on the following attributes:<br>
 * <ul>
 *     <li>contentType (required *) - only required if an extension manager is not defined to handle content lookup.
 *                                    If the content type is not found, it will try to retrieve content from any registered
 *                                    extension handlers.
 *                                    Specifies the content you are retrieving.</li>
 *     <li>contentName - if included will retrieve only content that matches the name.   When no name is specified,
 *                       all matching content items of the passed in type are retrieved.</li>
 *     <li>maxResults - if specified limits the results to a specified number of items.   The content will be returned
 *                 according to priority.   If content items share the same priority, then they will be returned
 *                 randomly.  Consider the example with 5 matching items with priorities (1,2,3,3,3) respectively.  If
 *                 the count is set to 3.   Items 1 and 2 will ALWAYS be returned.   The third item returned will
 *                 randomy rotate through the 3rd, 4th, and 5th item.
 *     </li>
 *     <li>contentListVar - allows you to specify an alternate name for the list of content results.   By default,
 *                          the results are returned in the page attributed "contentList"</li>
 *     <li>contentItemVar - since a typical usage is to only return one item, the first item is returned in the
 *                          variable "contentItem".   This variable can be used to change the attribute name.</li>
 *     <li>numResultsVar  - variable holding the returns the number of results being returned to through the tag-lib.
 *                          defaults to "numResults".</li>
 *     <li>fieldFilters  - Thymeleaf key-value pair to filter the resulting StructuredContentDTO by particular field values.
 *                          For instance, if you had a field in a piece of structured content called 'featured' and you
 *                          wanted to return all of the featured content items, you could do the following:
 *                          
 *                          <blc:content fieldFilters="featured=${'true'},otherField=${'someValue'}" />
 *     <li>sorts         - sorts to apply to the resulting list of content. These should be key-value pairs corresponding
 *                          where the key is the field to sort and the value is the direction of the sort. If unspecified,
 *                          the default sorting is used (by priority). The sort fields must occur in the dynamic fields
 *                          for that piece of structured content. For instance:
 *                          
 *                          <blc:content sort="dynamicFieldA='DESCENDING',dynamicFieldB='ASCENDING'" />
 *                          
 *                          The list will be sorted first by dynamicFieldA descending and then dynamicFieldB ascending
 * </ul>
 */
@Component("ucContentProcessor")
@ConditionalOnTemplating
public class ContentProcessor extends AbstractUltraVariableModifierProcessor {

    protected final Log LOG = LogFactory.getLog(getClass());
    public static final String REQUEST_DTO = "ucRequestDTO";
    public static final String UC_RULE_MAP_PARAM = "ucRuleMap";

    @Resource(name = "ucStructuredContentService")
    protected StructuredContentService structuredContentService;

    @Resource(name = "ucStaticAssetService")
    protected StaticAssetService staticAssetService;

    @Resource(name = "ucContentProcessorExtensionManager")
    protected ContentProcessorExtensionManager extensionManager;

    @Resource(name = "ucContentDeepLinkService")
    protected ContentDeepLinkServiceImpl contentDeepLinkService;

    @Override
    public String getName() {
        return "content";
    }
    
    @Override
    public int getPrecedence() {
        return 10000;
    }
    /**
     * Returns a default name
     * @param element
     * @param valueName
     * @return
     */
    protected String getAttributeValue(Map<String, String> tagAttributes, String valueName, String defaultValue) {
        String returnValue = tagAttributes.get(valueName);
        if (returnValue == null) {
            return defaultValue;
        } else {
            return returnValue;
        }
    }

    @Override
    public Map<String, Object> populateModelVariables(String tagName, Map<String, String> tagAttributes, UltraTemplateContext context) {
        String contentType = tagAttributes.get("contentType");
        String contentName = tagAttributes.get("contentName");
        String maxResultsStr = tagAttributes.get("maxResults");

        if (StringUtils.isEmpty(contentType) && StringUtils.isEmpty(contentName)) {
            throw new IllegalArgumentException("The content processor must have a non-empty attribute value for 'contentType' or 'contentName'");
        }

        Integer maxResults = null;
        if (maxResultsStr != null) {
            maxResults = Ints.tryParse(maxResultsStr);
        }
        if (maxResults == null) {
            maxResults = Integer.MAX_VALUE;
        }

        String contentListVar = getAttributeValue(tagAttributes, "contentListVar", "contentList");
        String contentItemVar = getAttributeValue(tagAttributes, "contentItemVar", "contentItem");
        String numResultsVar = getAttributeValue(tagAttributes, "numResultsVar", "numResults");

        String fieldFilters = tagAttributes.get("fieldFilters");
        final String sorts = tagAttributes.get("sorts");

        UltraRequestContext ucContext = UltraRequestContext.getUltraRequestContext();
        HttpServletRequest request = ucContext.getRequest();
        Map<String, Object> mvelParameters = buildMvelParameters(ucContext.getRequest(), tagAttributes, context);
        SandBox currentSandbox = ucContext.getSandBox();

        List<StructuredContentDTO> contentItems;
        StructuredContentType structuredContentType = null;
        if (contentType != null) {
            structuredContentType = structuredContentService.findStructuredContentTypeByName(contentType);
        }

        Locale locale = ucContext.getLocale();

        Map<String, Object> newModelVars = new HashMap<>();
        contentItems = getContentItems(contentName, maxResults, request, mvelParameters, currentSandbox, structuredContentType, locale, tagName, tagAttributes, newModelVars, context);
        
        if (contentItems.size() > 0) {

            // sort the resulting list by the configured property sorts on the tag
            if (StringUtils.isNotEmpty(sorts)) {
                final UltraTemplateContext finalContext = context;
                // In order to use the context in a comparator it needs to be final
                Collections.sort(contentItems, new Comparator<StructuredContentDTO>() {

                    @Override
                    public int compare(StructuredContentDTO o1, StructuredContentDTO o2) {
                        List<UltraAssignation> sortAssignments = finalContext.getAssignationSequence(sorts, false);
                        CompareToBuilder compareBuilder = new CompareToBuilder();
                        for (UltraAssignation sortAssignment : sortAssignments) {
                            String property = sortAssignment.getLeftStringRepresentation(finalContext);

                            Object val1 = o1.getPropertyValue(property);
                            Object val2 = o2.getPropertyValue(property);

                            if (sortAssignment.parseRight(finalContext).equals("ASCENDING")) {
                                compareBuilder.append(val1, val2);
                            } else {
                                compareBuilder.append(val2, val1);
                            }
                        }
                        return compareBuilder.toComparison();
                    }
                });
            }

            List<Map<String, Object>> contentItemFields = new ArrayList<>();

            for (StructuredContentDTO item : contentItems) {
                if (StringUtils.isNotEmpty(fieldFilters)) {
                    List<UltraAssignation> assignments = context.getAssignationSequence(fieldFilters, false);
                    boolean valid = true;
                    for (UltraAssignation assignment : assignments) {

                        if (ObjectUtils.notEqual(assignment.parseRight(context),
                            item.getValues().get(assignment.getLeftStringRepresentation(context)))) {
                            LOG.info("Excluding content " + item.getId() + " based on the property value of " + assignment.getLeftStringRepresentation(context));
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        contentItemFields.add(item.getValues());
                    }
                } else {
                    contentItemFields.add(item.getValues());
                }
            }

            Map<String, Object> contentItem = null;
            if (contentItemFields.size() > 0) {
                contentItem = contentItemFields.get(0);
            }

            newModelVars.put(contentItemVar, contentItem);
            newModelVars.put(contentListVar, contentItemFields);
            newModelVars.put(numResultsVar, contentItems.size());
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info("**************************The contentItems is null*************************");
            }
            newModelVars.put(contentItemVar, null);
            newModelVars.put(contentListVar, null);
            newModelVars.put(numResultsVar, 0);
        }

        String deepLinksVar = tagAttributes.get("deepLinks");
        if (StringUtils.isNotBlank(deepLinksVar) && contentItems.size() > 0) {
            List<DeepLink> links = contentDeepLinkService.getLinks(contentItems.get(0));
            extensionManager.getProxy().addExtensionFieldDeepLink(links, tagName, tagAttributes, context);
            extensionManager.getProxy().postProcessDeepLinks(links);
            newModelVars.put(deepLinksVar, links);
        }
        
        return newModelVars;
    }

    /**
     * @param contentName name of the content to be looked up (can be null)
     * @param maxResults maximum results to return
     * @param request servlet request
     * @param mvelParameters values that should be considered when filtering the content list by rules
     * @param structuredContentType the type of content that should be returned
     * @param locale current locale
     * @param arguments Thymeleaf Arguments passed into the tag
     * @param element element context that this Thymeleaf processor is being executed in
     * @return
     */
    protected List<StructuredContentDTO> getContentItems(String contentName, Integer maxResults, HttpServletRequest request,
                                                         Map<String, Object> mvelParameters,
                                                         SandBox currentSandbox,
                                                         StructuredContentType structuredContentType,
                                                         Locale locale, String tagName, Map<String, String> tagAttributes,
                                                         Map<String, Object> newModelVars, UltraTemplateContext context) {
        List<StructuredContentDTO> contentItems;
        if (structuredContentType == null) {
            contentItems = structuredContentService.lookupStructuredContentItemsByName(contentName, locale, maxResults, mvelParameters, isSecure(request));
        } else {
            if (contentName == null || "".equals(contentName)) {
                contentItems = structuredContentService.lookupStructuredContentItemsByType(structuredContentType, locale, maxResults, mvelParameters, isSecure(request));
            } else {
                contentItems = structuredContentService.lookupStructuredContentItemsByName(structuredContentType, contentName, locale, maxResults, mvelParameters, isSecure(request));
            }
        }

        //add additional fields to the model
        extensionManager.getProxy().addAdditionalFieldsToModel(tagName, tagAttributes, newModelVars, context);

        return contentItems;
    }

    /**
     * MVEL is used to process the content targeting rules.
     *
     * @param request
     * @return
     */
    protected Map<String, Object> buildMvelParameters(HttpServletRequest request, Map<String, String> tagAttributes, UltraTemplateContext context) {
        TimeZone timeZone = UltraRequestContext.getUltraRequestContext().getTimeZone();

        final TimeDTO timeDto;
        if (timeZone != null) {
            timeDto = new TimeDTO(SystemTime.asCalendar(timeZone));
        } else {
            timeDto = new TimeDTO();
        }

        RequestDTO requestDto = (RequestDTO) request.getAttribute(REQUEST_DTO);

        Map<String, Object> mvelParameters = new HashMap<>();
        mvelParameters.put("time", timeDto);
        mvelParameters.put("request", requestDto);

        String productString = tagAttributes.get("product");

        if (productString != null) {
            Object product = context.parseExpression(productString);
            if (product != null) {
                mvelParameters.put("product", product);
            }
        }

        String categoryString = tagAttributes.get("category");

        if (categoryString != null) {
            Object category = context.parseExpression(categoryString);
            if (category != null) {
                mvelParameters.put("category", category);
            }
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> ucRuleMap = (Map<String, Object>) request.getAttribute(UC_RULE_MAP_PARAM);
        if (ucRuleMap != null) {
            for (String mapKey : ucRuleMap.keySet()) {
                mvelParameters.put(mapKey, ucRuleMap.get(mapKey));
            }
        }

        return mvelParameters;
    }

    public boolean isSecure(HttpServletRequest request) {
        boolean secure = false;
        if (request != null) {
            secure = ("HTTPS".equalsIgnoreCase(request.getScheme()) || request.isSecure());
        }
        return secure;
    }

}
