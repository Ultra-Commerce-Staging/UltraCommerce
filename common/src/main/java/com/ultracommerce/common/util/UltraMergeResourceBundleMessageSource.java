/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.common.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ResourceLoader;

import java.text.MessageFormat;
import java.util.Locale;

import javax.annotation.Resource;


/**
 * <p>Replaces the deprecated {@link UCResourceBundleMessageSource} by using a {@link ReloadableResourceBundleMessageSource}
 * instead. The main advantage of using this is the out-of-the-box ability to merge multiple property files together. There
 * is one important difference: When there is a conflict for a property (declared in multiple files) this implementation
 * assumes that the <i>later</i> one in the list takes precedence. This follows with normal Ultra assumptions that bean
 * definitions declared later in the merge process win.</p>
 * 
 * <p>While this theoretically supports caching via the features provided in ReloadableResourceBundleMessageSource, this should
 * not be used and instead should have cacheMillis always set to -1 (which is the default implementation). This ensures
 * that codes are always obtained from a merged property list.</p>
 * 
 * <p>The basenames in this implementation are Spring path resources so if you need to refer to a resource on the classpath,
 * these should be prefixed with classpath:. This is slightly different from the {@link ResourceBundleMessageSource}; see
 * {@link ReloadableResourceBundleMessageSource#setBasenames(String...)} for more information.</p>
 *
 * <p>The {@link UltraMergeResourceExtensionManager} will get invoked first and return any
 * resolved message from an implementing module.</p>
 * 
 * @author Phillip Verheyden
 * @see {@link ReloadableResourceBundleMessageSource}
 * @see {@link ResourceLoader#getResource(String)}
 * @see {@link #setBasenames(String...)}
 */
public class UltraMergeResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

    @Resource(name = "ucUltraMergeResourceExtensionManager")
    protected UltraMergeResourceExtensionManager extensionManager;

    public UltraMergeResourceBundleMessageSource() {
        setDefaultEncoding("UTF-8");
    }
    
    /**
     * The super implementation ensures the basenames defined at the beginning take precedence. We require the opposite in
     * order to be in line with previous assumptions about the applicationContext merge process (meaning, beans defined in
     * later applicationContexts take precedence). Thus, this reverses <b>basenames</b> before passing it up to the super
     * implementation.
     * 
     * @param basenames
     * @param resourceBundleExtensionPoint
     * @see {@link ReloadableResourceBundleMessageSource#setBasenames(String...)}
     */
    @Resource(name="ucMessageSourceBaseNames")
    @Override
    public void setBasenames(String... basenames) {
        CollectionUtils.reverseArray(basenames);
        super.setBasenames(basenames);
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        ExtensionResultHolder<String> messageHolder = new ExtensionResultHolder<>();
        extensionManager.getProxy().resolveMessageSource(code, locale, messageHolder);
        if (StringUtils.isNotBlank(messageHolder.getResult())) {
            return createMessageFormat(messageHolder.getResult(), locale);
        }

        return super.resolveCode(code, locale);
    }

    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        ExtensionResultHolder<String> messageHolder = new ExtensionResultHolder<>();
        extensionManager.getProxy().resolveMessageSource(code, locale, messageHolder);
        if (StringUtils.isNotBlank(messageHolder.getResult())) {
            return messageHolder.getResult();
        }

        return super.resolveCodeWithoutArguments(code, locale);
    }

}
