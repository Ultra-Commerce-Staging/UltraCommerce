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
package com.ultracommerce.test;

import com.ultracommerce.common.util.TransactionUtils;
import com.ultracommerce.test.config.UltraSiteIntegrationTest;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * <p>
 * The {@literal @}Transactional version of the {@link TestNGSiteIntegrationSetup}. All {@literal @}Test methods contained
 * within any subclasses of this are run within the {@link TransactionUtils#DEFAULT_TRANSACTION_MANAGER} transaction manager.
 * However, all test-level transactions default to {@code @Rollback(true)}.
 * 
 * <p>
 * You can get finer-grained control over which classes are {@code @Transactional} and which ones aren't by instead subclassing
 * {@link TestNGSiteIntegrationSetup} instead and annotating individual {@code @Test} methods.
 * 
 * @see AbstractTransactionalTestNGSpringContextTests
 * @see TransactionalTestExecutionListener
 * @see UltraSiteIntegrationTest
 * @author Phillip Verheyden (phillipuniverse)
 */
@UltraSiteIntegrationTest
public abstract class TestNGTransactionalSiteIntegrationSetup extends AbstractTransactionalTestNGSpringContextTests {
    
}
