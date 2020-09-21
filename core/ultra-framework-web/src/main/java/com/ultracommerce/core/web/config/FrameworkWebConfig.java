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
package com.ultracommerce.core.web.config;

import com.ultracommerce.common.admin.condition.ConditionalOnNotAdmin;
import com.ultracommerce.common.security.util.CookieUtils;
import com.ultracommerce.common.web.filter.FilterOrdered;
import com.ultracommerce.core.rule.RuleDTOConfig;
import com.ultracommerce.core.web.cookie.CookieRuleFilter;
import com.ultracommerce.core.web.cookie.CookieRuleRequestProcessor;
import com.ultracommerce.core.web.seo.BasicSeoPropertyGeneratorImpl;
import com.ultracommerce.core.web.seo.SeoPropertyGenerator;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.web.filter.RequestContextFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeff Fischer
 */
@Configuration
public class FrameworkWebConfig {

    /**
     * Place a RequestContextFilter very high in the pre-security filter chain in order to guarantee
     * the RequestContext is set for any subsequent need. This should only impact Spring Boot implementations.
     *
     * @return the RequestFilter bean
     */
    @Bean
    public RequestContextFilter ucRequestContextFilter() {
        OrderedRequestContextFilter filter = new OrderedRequestContextFilter();
        filter.setOrder(FilterOrdered.PRE_SECURITY_HIGH - 1000);
        return filter;
    }
    
    @Bean
    public DeviceResolver ucDeviceResolver() {
        return new LiteDeviceResolver();
    }

    @Bean
    @ConditionalOnTemplating
    public List<SeoPropertyGenerator> ucSeoPropertyGenerators(@Qualifier("ucBasicSeoPropertyGenerator") BasicSeoPropertyGeneratorImpl basicSeo) {
        List<SeoPropertyGenerator> generators = new ArrayList<>();
        generators.add(basicSeo);
        return generators;
    }

    @Bean
    public ListFactoryBean ucCookieRuleConfigs() {
        ListFactoryBean listFactoryBean = new ListFactoryBean();
        listFactoryBean.setSourceList(new ArrayList<RuleDTOConfig>());
        return listFactoryBean;
    }

    @Bean
    @Autowired
    @ConditionalOnProperty("cookie.content.targeting.enabled")
    @ConditionalOnNotAdmin
    public CookieRuleRequestProcessor ucCookieRuleRequestProcessor(@Qualifier("ucCookieRuleConfigs") List configs, CookieUtils cookieUtils) {
        CookieRuleRequestProcessor processor = new CookieRuleRequestProcessor(configs, cookieUtils);
        return processor;
    }

    @Bean
    @Autowired
    @ConditionalOnProperty("cookie.content.targeting.enabled")
    @ConditionalOnNotAdmin
    public CookieRuleFilter ucCookieRuleFilter(@Qualifier("ucCookieRuleRequestProcessor") CookieRuleRequestProcessor processor) {
        CookieRuleFilter filter = new CookieRuleFilter(processor);
        return filter;
    }
}
