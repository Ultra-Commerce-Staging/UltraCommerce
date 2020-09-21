/*
 * #%L
 * UltraCommerce Integration
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
package com.ultracommerce.common.email.service;

import com.ultracommerce.common.email.service.info.EmailInfo;
import com.ultracommerce.test.TestNGSiteIntegrationSetup;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import javax.annotation.Resource;

public class EmailTest extends TestNGSiteIntegrationSetup {

    @Resource
    EmailService emailService;
    
    private GreenMail greenMail;

    @BeforeClass
    protected void setupEmailTest() {
        greenMail = new GreenMail(
                new ServerSetup[] {
                        new ServerSetup(30000, "127.0.0.1", ServerSetup.PROTOCOL_SMTP)
                }
        );
        greenMail.start();
    }

    @AfterClass
    protected void tearDownEmailTest() {
        greenMail.stop();
    }

    @Test
    public void testSynchronousEmail() throws Exception {
        EmailInfo info = new EmailInfo();
        info.setFromAddress("me@test.com");
        info.setSubject("test");
        info.setEmailTemplate("com/ultracommerce/common/email/service/template/default.vm");
        info.setSendEmailReliableAsync("false");

        emailService.sendTemplateEmail("to@localhost", info, null);
    }

}
