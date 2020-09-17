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
package com.ultracommerce.cms.web;

import junit.framework.TestCase;

/**
 * Created by bpolster.
 */
public class UltraProcessURLFilterTest extends TestCase {
    public void testShouldProcessURL() throws Exception {
        UltraProcessURLFilter cf = new UltraProcessURLFilter();
        // Should fail
        assertFalse("Image resource should not be processed by content filter.", cf.shouldProcessURL(null, "/path/subpath/test.jpg"));
        assertFalse("URLs containing com.ultracommerce.admin should not be processed.", cf.shouldProcessURL(null, "/path/com.ultracommerce.admin/admintest"));
        assertTrue("/about_us should be processed by the content filter", cf.shouldProcessURL(null, "/about_us"));
        assertTrue("*.htm resources should be processed by the content filter", cf.shouldProcessURL(null, "/test.htm"));
    }
}
