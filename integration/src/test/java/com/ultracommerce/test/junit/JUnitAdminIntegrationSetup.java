/*
 * #%L
 * UltraCommerce Integration
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
package com.ultracommerce.test.junit;

import com.ultracommerce.test.config.UltraAdminIntegrationTest;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * <p>
 * Used as a customization point so that you can extend this class and add additional beans to the live Ultra ApplicationContext.
 * This also adds the additional default {@link TestExecutionListener}s that are not included in the Spring super class.
 * 
 * <p>
 * This class hierarchy <b>must</b> be used if you are making any customizations to the ApplicationContext. If you do not need
 * any addtiional customizations for your tests then you can just use {@literal @}UltraAdminIntegrationTest directly.
 * 
 * @see UltraAdminIntegrationTest
 * @see JUnitTransactionalAdminIntegrationSetup
 * @author Phillip Verheyden (phillipuniverse)
 */
@UltraAdminIntegrationTest
@TestExecutionListeners({TransactionalTestExecutionListener.class, SqlScriptsTestExecutionListener.class})
public class JUnitAdminIntegrationSetup extends AbstractJUnit4SpringContextTests {

}
