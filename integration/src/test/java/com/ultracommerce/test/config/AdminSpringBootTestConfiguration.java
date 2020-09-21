/*-
 * #%L
 * ultra-marketplace
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
/**
 * 
 */
package com.ultracommerce.test.config;

import com.ultracommerce.common.email.service.info.EmailInfo;
import com.ultracommerce.common.email.service.message.MessageCreator;
import com.ultracommerce.common.email.service.message.NullMessageCreator;
import com.ultracommerce.common.extensibility.context.merge.Merge;
import com.ultracommerce.common.web.filter.IgnorableOpenEntityManagerInViewFilter;
import com.ultracommerce.test.helper.AdminTestHelper;
import com.ultracommerce.test.helper.TestAdminRequestFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockitoPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * Marketplace spring boot integration test config
 * 
 * @author Jeff Fischer
 */
@TestConfiguration
public class AdminSpringBootTestConfiguration {

    @Bean(name = "org.springframework.boot.test.mock.mockito.MockitoPostProcessor")
    public static BeanFactoryPostProcessor mockitoNoOp() {
        return new NoOpMockitoPostProcessor();
    }

    @Autowired
    @Qualifier("webDS")
    DataSource webDS;

    @Autowired
    @Qualifier("webSecureDS")
    DataSource webSecureDS;

    @Autowired
    @Qualifier("webStorageDS")
    DataSource webStorageDS;

    @Bean
    public MapFactoryBean ucMergedDataSources() throws Exception {
        MapFactoryBean mapFactoryBean = new MapFactoryBean();
        Map<String, DataSource> sourceMap = new HashMap<>();
        sourceMap.put("jdbc/test", webDS);
        sourceMap.put("jdbc/testSecure", webSecureDS);
        sourceMap.put("jdbc/testCMS", webStorageDS);
        mapFactoryBean.setSourceMap(sourceMap);

        return mapFactoryBean;
    }

    @Merge(targetRef = "ucMergedPersistenceXmlLocations", early = true)
    public List<String> corePersistenceXmlLocations() {
        return Arrays.asList("classpath*:/META-INF/persistence-test.xml");
    }

    /**
     * A dummy mail sender has been set to send emails for testing purposes only
     * To view the emails sent use "DevNull SMTP" (download separately) with the following setting:
     *   Port: 30000
     */
    @Bean
    public MailSender ucMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("localhost");
        sender.setPort(30000);
        sender.setProtocol("smtp");
        Properties javaMailProps = new Properties();
        javaMailProps.setProperty("mail.smtp.starttls.enable", "true");
        javaMailProps.setProperty("mail.smtp.timeout", "25000");
        sender.setJavaMailProperties(javaMailProps);
        return sender;
    }

    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
        IgnorableOpenEntityManagerInViewFilter filter = new IgnorableOpenEntityManagerInViewFilter();
        return filter;
    }

    @Bean
    public TestAdminRequestFilter ucTestAdminRequestFilter() {
        return new TestAdminRequestFilter();
    }

    @Bean
    @Autowired
    public MessageCreator ucMessageCreator(@Qualifier("ucMailSender") JavaMailSender mailSender) {
        return new NullMessageCreator(mailSender);
    }

    @Bean
    public EmailInfo ucEmailInfo() {
        EmailInfo info = new EmailInfo();
        info.setFromAddress("support@mycompany.com");
        info.setSendAsyncPriority("2");
        info.setSendEmailReliableAsync("false");
        return info;
    }

    @Bean
    public EmailInfo ucRegistrationEmailInfo() {
        EmailInfo info = ucEmailInfo();
        info.setSubject("You have successfully registered!");
        info.setEmailTemplate("register-email");
        return info;
    }

    @Bean
    public EmailInfo ucForgotPasswordEmailInfo() {
        EmailInfo info = ucEmailInfo();
        info.setSubject("Reset password request");
        info.setEmailTemplate("resetPassword-email");
        return info;
    }

    @Bean
    public EmailInfo ucOrderConfirmationEmailInfo() {
        EmailInfo info = ucEmailInfo();
        info.setSubject("Your order with The Heat Clinic");
        info.setEmailTemplate("orderConfirmation-email");
        return info;
    }

    @Bean
    public EmailInfo ucFulfillmentOrderTrackingEmailInfo() {
        EmailInfo info = ucEmailInfo();
        info.setSubject("Your order with The Heat Clinic Has Shipped");
        info.setEmailTemplate("fulfillmentOrderTracking-email");
        return info;
    }

    @Bean
    public EmailInfo ucReturnAuthorizationEmailInfo() {
        EmailInfo info = ucEmailInfo();
        info.setSubject("Your return with The Heat Clinic");
        info.setEmailTemplate("returnAuthorization-email");
        return info;
    }

    @Bean
    public EmailInfo ucReturnConfirmationEmailInfo() {
        EmailInfo info = ucEmailInfo();
        info.setSubject("Your return with The Heat Clinic");
        info.setEmailTemplate("returnConfirmation-email");
        return info;
    }

    @Bean
    public AdminTestHelper ucAdminTestHelper() {
        return new AdminTestHelper();
    }
}
