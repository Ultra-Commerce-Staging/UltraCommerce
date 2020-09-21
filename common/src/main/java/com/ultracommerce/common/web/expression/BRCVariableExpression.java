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
package com.ultracommerce.common.web.expression;

import org.apache.commons.beanutils.PropertyUtils;
import com.ultracommerce.common.crossapp.service.CrossAppAuthService;
import com.ultracommerce.common.sandbox.domain.SandBox;
import com.ultracommerce.common.site.domain.Catalog;
import com.ultracommerce.common.site.domain.Site;
import com.ultracommerce.common.time.SystemTime;
import com.ultracommerce.common.util.UCRequestUtils;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.ultracommerce.common.web.UltraSandBoxResolverImpl.CLIENT_TIMEZONE;


/**
 * Exposes the {@link UltraRequestContext} to the Thymeleaf expression context
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Component("ucBRCVariableExpression")
@ConditionalOnTemplating
public class BRCVariableExpression implements UltraVariableExpression {
    
    @Autowired(required = false)
    @Qualifier("ucCrossAppAuthService")
    protected CrossAppAuthService crossAppAuthService;

    @Override
    public String getName() {
        return "brc";
    }
    
    public SandBox getSandbox() {
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        if (brc != null) {
            return brc.getSandBox();
        }
        return null;
    }

    public Site getSite() {
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        if (brc != null) {
            return brc.getNonPersistentSite();
        }
        return null;
    }

    public Site getCurrentProfile() {
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        if (brc != null) {
            return brc.getCurrentProfile();
        }
        return null;
    }

    public Catalog getCurrentCatalog() {
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        if (brc != null) {
            return brc.getCurrentCatalog();
        }
        return null;
    }
    
    public Date getCurrentTime() {
        return SystemTime.asDate(true);
    }

    public Calendar getCurrentTimeCalendar() {

        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();

        WebRequest webRequest = brc.getWebRequest();

        if(UCRequestUtils.isOKtoUseSession(webRequest)) {
            HttpSession session = brc.getRequest().getSession();
            TimeZone timeZone = (TimeZone) session.getAttribute(CLIENT_TIMEZONE);


            if(timeZone != null) {

                return SystemTime.asCalendar(Locale.getDefault(), timeZone, true);
            }
        }

        return SystemTime.asCalendar(true);

    }
    
    public Object get(String propertyName) {
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        if (brc != null) {
            try {
                return PropertyUtils.getProperty(brc, propertyName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public boolean isCsrMode() {
        return crossAppAuthService == null ? false : crossAppAuthService.hasCsrPermission();
    }

    public boolean isQuoteMode() {
        return crossAppAuthService == null ? false : crossAppAuthService.hasQuotePermission();
    }

    public boolean isSandboxMode() {
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        return (brc == null) ? false : (brc.getSandBox() != null);
    }
    
    public Object getAdditionalProperty(String propertyName) {
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
        if (brc != null) {
            return brc.getAdditionalProperties().get(propertyName);
        }
        return null;
    }

}
