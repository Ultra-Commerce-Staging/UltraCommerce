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

import com.ultracommerce.cms.file.service.StaticAssetService;
import com.ultracommerce.common.file.service.StaticAssetPathService;
import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import com.ultracommerce.presentation.dialect.AbstractUltraAttributeModifierProcessor;
import com.ultracommerce.presentation.model.UltraAttributeModifier;
import com.ultracommerce.presentation.model.UltraTemplateContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * A Thymeleaf processor that processes the given url through the StaticAssetService's
 * {@link StaticAssetService#convertAssetPath(String, String, boolean)} method to determine
 * the appropriate URL for the asset to be served from.
 *
 * @author apazzolini
 */
@Component("ucUrlRewriteProcessor")
@ConditionalOnTemplating
public class UrlRewriteProcessor extends AbstractUltraAttributeModifierProcessor {

    @Resource(name = "ucStaticAssetPathService")
    protected StaticAssetPathService staticAssetPathService;

    @Override
    public String getName() {
        return "src";
    }

    @Override
    public int getPrecedence() {
        return 1000;
    }

    @Override
    public UltraAttributeModifier getModifiedAttributes(String tagName, Map<String, String> tagAttributes, String attributeName,
            String attributeValue, UltraTemplateContext context) {
        Map<String, String> newAttributes = new HashMap<>();
        newAttributes.put("src", getFullAssetPath(tagName, attributeValue, context));
        return new UltraAttributeModifier(newAttributes);
    }

    protected String getFullAssetPath(String tagName, String attributeValue, UltraTemplateContext context) {
        HttpServletRequest request = UltraRequestContext.getUltraRequestContext().getRequest();
        boolean secureRequest = true;
        if (request != null) {
            secureRequest = isRequestSecure(request);
        }

        String assetPath = parsePath(attributeValue, context);

        String extension = getFileExtension(assetPath);
        if (isImageTag(tagName) && isAdminRequest() && !isImageExtension(extension)) {
            String defaultFileTypeImagePath = getDefaultFileTypeImagePath(extension);
            String queryString = getQueryString(assetPath);

            assetPath = parsePath(defaultFileTypeImagePath + queryString, context);
        }

        // We are forcing an evaluation of @{} from Thymeleaf above which will automatically add a contextPath,
        // no need to add it twice
        return staticAssetPathService.convertAssetPath(assetPath, null, secureRequest);
    }

    /**
     * @return true if the current request.scheme = HTTPS or if the request.isSecure value is true.
     */
    protected boolean isRequestSecure(HttpServletRequest request) {
        return ("HTTPS".equalsIgnoreCase(request.getScheme()) || request.isSecure());
    }

    protected boolean isImageTag(String tagName) {
        return "img".equals(tagName);
    }

    protected boolean isAdminRequest() {
        return UltraRequestContext.getUltraRequestContext().getAdmin();
    }

    protected Boolean isImageExtension(String extension) {
        String imageExtensions = UCSystemProperty.resolveSystemProperty("admin.image.file.extensions");

        return imageExtensions.contains(extension);
    }

    protected String parsePath(String attributeValue, UltraTemplateContext context) {
        String newAttributeValue = attributeValue;
        if (newAttributeValue.startsWith("/")) {
            newAttributeValue = "@{ " + newAttributeValue + " }";
        }
        return (String) context.parseExpression(newAttributeValue);
    }

    protected String getFileExtension(String assetPath) {
        String extension;

        int extensionStartIndex = assetPath.lastIndexOf(".") + 1;
        int queryStartIndex = assetPath.lastIndexOf("?");
        if (queryStartIndex > 0) {
            extension = assetPath.substring(extensionStartIndex, queryStartIndex);
        } else {
            extension = assetPath.substring(extensionStartIndex);
        }

        return extension.toLowerCase();
    }

    protected String getDefaultFileTypeImagePath(String extension) {
        String imageUrl;
        switch (extension) {
            case "txt":
                imageUrl = "/img/admin/file-txt.png";
                break;
            case "pdf":
                imageUrl = "/img/admin/file-pdf.png";
                break;
            case "doc":
            case "docx":
                imageUrl = "/img/admin/file-doc.png";
                break;
            case "xls":
            case "xlsx":
                imageUrl = "/img/admin/file-xls.png";
                break;
            case "ppt":
            case "pptx":
                imageUrl = "/img/admin/file-ppt.png";
                break;
            default:
                imageUrl = "/img/admin/file-unkn.png";
        }
        return imageUrl;
    }

    protected String getQueryString(String assetPath) {
        int queryStartIndex = assetPath.lastIndexOf("?");

        return (queryStartIndex > 0) ? assetPath.substring(queryStartIndex) : "";
    }
}
