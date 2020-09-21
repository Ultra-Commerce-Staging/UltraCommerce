/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.common.event;

import com.ultracommerce.common.currency.domain.UltraCurrency;
import com.ultracommerce.common.currency.service.UltraCurrencyService;
import com.ultracommerce.common.locale.domain.Locale;
import com.ultracommerce.common.locale.service.LocaleService;
import com.ultracommerce.common.site.domain.Catalog;
import com.ultracommerce.common.site.domain.Site;
import com.ultracommerce.common.site.service.SiteService;
import com.ultracommerce.common.util.tenant.IdentityExecutionUtils;
import com.ultracommerce.common.util.tenant.IdentityOperation;
import com.ultracommerce.common.web.UltraRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.TimeZone;

/**
 * This abstract class contains the plumbing that sets up the context for handling a {@code UltraApplicationEvent}.
 *
 * @author Nick Crum ncrum
 */
public abstract class AbstractUltraApplicationEventListener<T extends UltraApplicationEvent>
        implements UltraApplicationListener<T> {

    @Autowired
    @Qualifier("ucSiteService")
    protected SiteService siteService;

    @Autowired
    @Qualifier("ucCurrencyService")
    protected UltraCurrencyService currencyService;

    @Autowired
    @Qualifier("ucLocaleService")
    protected LocaleService localeService;

    protected abstract void handleApplicationEvent(T event);

    @Override
    public final void onApplicationEvent(final T event) {
        Site site = getSite(event);
        Catalog catalog = getCatalog(event);
        Site profile = getProfile(event);

        IdentityExecutionUtils.runOperationByIdentifier(new IdentityOperation<Void,RuntimeException>() {
            @Override
            public Void execute() throws RuntimeException {
                UltraRequestContext ctx = UltraRequestContext.getUltraRequestContext();
                TimeZone origTimeZone = ctx.getTimeZone();
                Locale origLocale = ctx.getLocale();
                UltraCurrency origCurrency = ctx.getUltraCurrency();
                try {
                    ctx.setTimeZone(getTimeZone(event));
                    ctx.setLocale(getLocale(event));
                    ctx.setUltraCurrency(getCurrency(event));
                    handleApplicationEvent(event);
                } finally {
                    ctx.setTimeZone(origTimeZone);
                    ctx.setLocale(origLocale);
                    ctx.setUltraCurrency(origCurrency);
                }
                return null;
            }
        }, site, profile, catalog);
    }

    protected Site getSite(T event) {
        if (event.getSiteId() != null) {
            return siteService.retrieveNonPersistentSiteById(event.getSiteId());
        }
        return null;
    }

    protected Catalog getCatalog(T event) {
        if (event.getCatalogId() != null) {
            return siteService.findCatalogById(event.getCatalogId());
        }
        return null;
    }

    protected Site getProfile(T event) {
        if (event.getProfileId() != null) {
            return siteService.retrieveNonPersistentSiteById(event.getProfileId());
        }
        return null;
    }

    protected UltraCurrency getCurrency(T event) {
        if (event.getCurrencyCode() != null) {
            return currencyService.findCurrencyByCode(event.getCurrencyCode());
        }
        return null;
    }

    protected Locale getLocale(T event) {
        if (event.getLocaleCode() != null) {
            return localeService.findLocaleByCode(event.getLocaleCode());
        }
        return null;
    }

    protected TimeZone getTimeZone(T event) {
        if (event.getTimeZoneId() != null) {
            return TimeZone.getTimeZone(event.getTimeZoneId());
        }
        return null;
    }
}
