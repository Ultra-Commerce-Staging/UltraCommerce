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
package com.ultracommerce.common.audit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.util.UCFieldUtils;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.common.web.UltraRequestCustomerResolverImpl;

import java.lang.reflect.Field;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AuditableListener extends AbstractAuditableListener {

    private static final Log LOG = LogFactory.getLog(AuditableListener.class);

    @PrePersist
    @Override
    public void setAuditCreationAndUpdateData(Object entity) throws Exception {
        setAuditCreationData(entity, new Auditable());
        setAuditUpdateData(entity, new Auditable());
    }

    @PreUpdate
    @Override
    public void setAuditUpdateData(Object entity) throws Exception {
        setAuditUpdateData(entity, new Auditable());
    }

    @Override
    protected void setAuditValueAgent(Field field, Object entity) throws IllegalArgumentException, IllegalAccessException {
        try {
            UltraRequestContext context = UltraRequestContext.getUltraRequestContext();
            if (context != null && context.getAdmin() && context.getAdminUserId() != null) {
                field.setAccessible(true);
                field.set(entity, context.getAdminUserId());
            } else if (context != null && context.getWebRequest() != null) {
                Long customerId = 0L;
                Object customer = UltraRequestCustomerResolverImpl.getRequestCustomerResolver().getCustomer();

                if (customer != null) {
                    Class<?> customerClass = customer.getClass();
                    Field userNameField = UCFieldUtils.getSingleField(customerClass, "username");
                    userNameField.setAccessible(true);
                    String username = (String) userNameField.get(customer);
                    if (username != null) {
                        //the customer has been persisted
                        Field idField = UCFieldUtils.getSingleField(customerClass, "id");
                        idField.setAccessible(true);
                        customerId = (Long) idField.get(customer);
                    }
                }

                field.setAccessible(true);
                field.set(entity, customerId);
            }
        } catch (Exception e) {
            LOG.error("Error setting audit field.", e);
        }
    }

}
