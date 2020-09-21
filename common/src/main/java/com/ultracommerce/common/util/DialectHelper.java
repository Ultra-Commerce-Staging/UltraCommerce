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
package com.ultracommerce.common.util;

import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Jeff Fischer
 */
@Repository("ucDialectHelper")
public class DialectHelper {

    @PersistenceContext(unitName="ucPU")
    protected EntityManager defaultEntityManager;


    public Dialect getHibernateDialect() {
        return getHibernateDialect(defaultEntityManager);
    }

    public Dialect getHibernateDialect(EntityManager em) {
        SessionFactoryImplementor factory = (SessionFactoryImplementor) em.unwrap(Session.class).getSessionFactory();
        return factory.getDialect();
    }

    public boolean isOracle() {
        //This should handle other Oracle dialects as well, since they derive from Oracle8iDialect
        return getHibernateDialect(defaultEntityManager) instanceof Oracle8iDialect;
    }

    public boolean isOracle(EntityManager em) {
        //This should handle other Oracle dialects as well, since they derive from Oracle8iDialect
        return getHibernateDialect(em) instanceof Oracle8iDialect;
    }

    public boolean isPostgreSql() {
        //This should handle other Postgres dialects as well, since they derive from PostgreSQL81Dialect
        return getHibernateDialect(defaultEntityManager) instanceof PostgreSQL81Dialect;
    }

    public boolean isPostgreSql(EntityManager em) {
        //This should handle other Postgres dialects as well, since they derive from PostgreSQL81Dialect
        return getHibernateDialect(em) instanceof PostgreSQL81Dialect;
    }

    public boolean isSqlServer() {
        return getHibernateDialect(defaultEntityManager) instanceof SQLServerDialect;
    }

    public boolean isSqlServer(EntityManager em) {
        return getHibernateDialect(em) instanceof SQLServerDialect;
    }

    public boolean isMySql() {
        return getHibernateDialect(defaultEntityManager) instanceof MySQLDialect;
    }

    public boolean isMySql(EntityManager em) {
        return getHibernateDialect(em) instanceof MySQLDialect;
    }
}
