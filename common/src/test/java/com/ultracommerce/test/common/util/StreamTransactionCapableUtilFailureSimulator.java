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
package com.ultracommerce.test.common.util;

import com.ultracommerce.common.util.StreamingTransactionCapableUtil;
import com.ultracommerce.common.web.UltraRequestContext;
import org.hibernate.SessionFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.EntityType;

/**
 * Utility class that can be substituted for StreamingTransactionCapableUtil to allow targeted testing of
 * transaction failures (e.g. connection pool exhaustion)
 *
 * @author Jeff Fischer
 */
public class StreamTransactionCapableUtilFailureSimulator extends StreamingTransactionCapableUtil {

    public static final String FAILURE_MODE_KEY = "failureMode";
    public static final String FAILURE_MODE_PU = "failureModePU";
    public static final String FAILURE_MODE_EXCEPTION = "failureModeException";
    private static final String ucPUCheckClassName = "com.ultracommerce.core.catalog.domain.ProductImpl";
    private static final String ucEventPUCheckClassName = "com.ultracommerce.jobsevents.domain.SystemEventImpl";

    public void startFailureMode(RuntimeException exceptionToThrow, String persistenceUnit) {
        checkPU(persistenceUnit);
        UltraRequestContext context = UltraRequestContext.getUltraRequestContext();
        context.getAdditionalProperties().put(FAILURE_MODE_KEY, true);
        context.getAdditionalProperties().put(FAILURE_MODE_PU, persistenceUnit);
        context.getAdditionalProperties().put(FAILURE_MODE_EXCEPTION, exceptionToThrow);
    }

    public void endFailureMode(String persistenceUnit) {
        checkPU(persistenceUnit);
        UltraRequestContext context = UltraRequestContext.getUltraRequestContext();
        context.getAdditionalProperties().remove(FAILURE_MODE_KEY);
        context.getAdditionalProperties().remove(FAILURE_MODE_PU);
        context.getAdditionalProperties().remove(FAILURE_MODE_EXCEPTION);
    }

    @Override
    protected TransactionStatus startTransaction(int propagationBehavior, int isolationLevel, boolean readOnly, PlatformTransactionManager transactionManager) {
        UltraRequestContext context = UltraRequestContext.getUltraRequestContext();
        if (context.getAdditionalProperties().containsKey(FAILURE_MODE_KEY)) {
            String failureModePU = (String) context.getAdditionalProperties().get(FAILURE_MODE_PU);
            String checkClassName = failureModePU.equals("ucPU")?ucPUCheckClassName:ucEventPUCheckClassName;
            List<String> entities = new ArrayList<>();
            for (EntityType<?> item : ((JpaTransactionManager) transactionManager).getEntityManagerFactory().unwrap(SessionFactory.class).getMetamodel().getEntities())  {
                entities.add(item.getJavaType().getName());
            }
            if (entities.contains(checkClassName)){
                throw (RuntimeException) context.getAdditionalProperties().get(FAILURE_MODE_EXCEPTION);
            }
        }
        return super.startTransaction(propagationBehavior, isolationLevel, readOnly, transactionManager);
    }

    protected void checkPU(String persistenceUnit) {
        if (!persistenceUnit.equals("ucPU") && !persistenceUnit.equals("ucEventPU")) {
            throw new UnsupportedOperationException(persistenceUnit + " not supported");
        }
    }
}
