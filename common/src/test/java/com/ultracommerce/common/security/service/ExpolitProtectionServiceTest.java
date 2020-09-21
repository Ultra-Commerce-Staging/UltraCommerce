/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2019 Ultra Commerce
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
package com.ultracommerce.common.security.service;

import com.ultracommerce.common.exception.ServiceException;

import junit.framework.TestCase;

public class ExpolitProtectionServiceTest extends TestCase {
    public void test(){
        ExploitProtectionServiceImpl service = new ExploitProtectionServiceImpl();
        service.setXssProtectionEnabled(true);
        try {
            String s = service.cleanStringWithResults("\"javascript:alert(0)");
        } catch (ServiceException e) {
            assertTrue(e instanceof CleanStringException);
        }
        try {
            String testString = "aaa";
            String s = service.cleanStringWithResults(testString);
            assertEquals(s, testString);
        } catch (ServiceException e) {
            fail("Exception is not expected");
        }
    }
}
