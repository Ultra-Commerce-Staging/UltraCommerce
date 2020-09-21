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
package com.ultracommerce.common.demo;

import org.springframework.core.Ordered;

/**
 * Allows a module to specify a sql file and some ordering information to use during Hibernate auto import. This information
 * is traditionally set via the 'hibernate.hbm2ddl.auto' property. However, that method is flat and does not allow
 * easy individual module contribution and special ordering. By using this bean, a module can identify a sql file to
 * import and specify the order it should appear in the cumulative list of all module contributions.
 *
 * @author Jeff Fischer
 */
public class AutoImportSql implements Ordered {

    protected String sqlFilePath;
    protected int order = AutoImportStage.PRIMARY_LATE;
    protected String persistenceUnit;

    public AutoImportSql() {
    }

    public AutoImportSql(String persistenceUnit, String sqlFilePath, int order) {
        this.persistenceUnit = persistenceUnit;
        this.sqlFilePath = sqlFilePath;
        this.order = order;
    }

    public String getSqlFilePath() {
        return sqlFilePath;
    }

    /**
     * Set one or more path locations to sql files. Multiple files are separated with a comma.
     *
     * @param sqlFilePath
     */
    public void setSqlFilePath(String sqlFilePath) {
        this.sqlFilePath = sqlFilePath;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPersistenceUnit() {
        return persistenceUnit;
    }

    public void setPersistenceUnit(String persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }
}
