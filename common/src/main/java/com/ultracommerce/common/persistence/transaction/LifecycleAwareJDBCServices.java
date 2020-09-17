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
package com.ultracommerce.common.persistence.transaction;

import org.hibernate.engine.jdbc.internal.JdbcServicesImpl;
import org.hibernate.engine.jdbc.spi.SqlStatementLogger;

/**
 * A customization of {@link JdbcServicesImpl} that allows further usage of the sql statement logging. This is useful
 * for tracking all sql executed during the lifecycle of a transaction.
 *
 * @see TransactionLifecycleAwareSqlStatementLogger
 * @see TransactionLifecycleMonitor
 * @author Jeff Fischer
 */
public class LifecycleAwareJDBCServices extends JdbcServicesImpl {

    @Override
    public SqlStatementLogger getSqlStatementLogger() {
        SqlStatementLogger defaultLogger = super.getSqlStatementLogger();
        return new TransactionLifecycleAwareSqlStatementLogger(defaultLogger.isLogToStdout(), defaultLogger.isFormat());
    }
}
