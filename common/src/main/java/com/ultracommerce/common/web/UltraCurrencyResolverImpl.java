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
package com.ultracommerce.common.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.common.currency.domain.UltraRequestedCurrencyDto;
import com.ultracommerce.common.currency.service.UltraCurrencyService;
import com.ultracommerce.common.locale.domain.Locale;
import com.ultracommerce.common.util.UCRequestUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Author: jerryocanas
 * Date: 9/6/12
 */

/**
 * Responsible for returning the currency to use for the current request.
 */
@Component("ucCurrencyResolver")
public class UltraCurrencyResolverImpl implements UltraCurrencyResolver {

    private final Log LOG = LogFactory.getLog(UltraCurrencyResolverImpl.class);

    /**
     * Parameter/Attribute name for the current currency code
     */
    public static String CURRENCY_CODE_PARAM = "ucCurrencyCode";

    /**
     * Parameter/Attribute name for the current currency
     */
    public static String CURRENCY_VAR = "ucCurrency";

    @Resource(name = "ucCurrencyService")
    private UltraCurrencyService ultraCurrencyService;

    /**
     * Responsible for returning the currency to use for the current request.
     */
    @Override
    public UltraRequestedCurrencyDto resolveCurrency(HttpServletRequest request) {
        return resolveCurrency(new ServletWebRequest(request));
    }

    @Override
    public UltraRequestedCurrencyDto resolveCurrency(WebRequest request) {
        UltraCurrency desiredCurrency = null;

        // 1) Check request for currency
        desiredCurrency = (UltraCurrency) request.getAttribute(CURRENCY_VAR, WebRequest.SCOPE_REQUEST);

        // 2) Check for a request parameter
        if (desiredCurrency == null && UCRequestUtils.getURLorHeaderParameter(request, CURRENCY_CODE_PARAM) != null) {
            String currencyCode = UCRequestUtils.getURLorHeaderParameter(request, CURRENCY_CODE_PARAM);
            desiredCurrency = ultraCurrencyService.findCurrencyByCode(currencyCode);
            if (LOG.isTraceEnabled()) {
                LOG.trace("Attempt to find currency by param " + currencyCode + " resulted in " + desiredCurrency);
            }
        }

        // 3) Check session for currency
        if (desiredCurrency == null && UCRequestUtils.isOKtoUseSession(request)) {
            desiredCurrency = (UltraCurrency) request.getAttribute(CURRENCY_VAR, WebRequest.SCOPE_SESSION);
        }

        // 4) Check locale for currency
        if (desiredCurrency == null) {
            Locale locale = (Locale) request.getAttribute(UltraLocaleResolverImpl.LOCALE_VAR, WebRequest.SCOPE_REQUEST);
            if (locale != null) {
                desiredCurrency = locale.getDefaultCurrency();
            }
        }

        // 5) Lookup default currency from DB
        UltraCurrency defaultCurrency = ultraCurrencyService.findDefaultUltraCurrency();
        if (desiredCurrency == null) {
            desiredCurrency = defaultCurrency;
        }

        // For an out-of-box installation, only one currency is supported, so even though we have a 
        // desired currency, we may not have any prices that support it. 
        UltraCurrency currencyToUse = defaultCurrency;

        if (UCRequestUtils.isOKtoUseSession(request)) {
            request.setAttribute(CURRENCY_VAR, currencyToUse, WebRequest.SCOPE_SESSION);
        }

        UltraRequestedCurrencyDto dto = new UltraRequestedCurrencyDto(currencyToUse, desiredCurrency);
        return dto;
    }



}
