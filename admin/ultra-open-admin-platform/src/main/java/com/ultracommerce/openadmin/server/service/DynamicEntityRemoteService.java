/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.server.service;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.security.service.CleanStringException;
import com.ultracommerce.common.security.service.ExploitProtectionService;
import com.ultracommerce.common.service.PersistenceService;
import com.ultracommerce.common.util.StreamCapableTransactionalOperationAdapter;
import com.ultracommerce.common.util.StreamingTransactionCapableUtil;
import com.ultracommerce.openadmin.dto.BatchDynamicResultSet;
import com.ultracommerce.openadmin.dto.BatchPersistencePackage;
import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.dto.Property;
import com.ultracommerce.openadmin.server.service.persistence.Persistable;
import com.ultracommerce.openadmin.server.service.persistence.PersistenceManager;
import com.ultracommerce.openadmin.server.service.persistence.PersistenceManagerFactory;
import com.ultracommerce.openadmin.server.service.persistence.PersistenceResponse;
import com.ultracommerce.openadmin.server.service.persistence.PersistenceThreadManager;
import com.ultracommerce.common.persistence.TargetModeType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @author jfischer
 */
@Service("ucDynamicEntityRemoteService")
public class DynamicEntityRemoteService implements DynamicEntityService {

    private static final Log LOG = LogFactory.getLog(DynamicEntityRemoteService.class);
    protected static final Map<BatchPersistencePackage, BatchDynamicResultSet> METADATA_CACHE = Collections.synchronizedMap(new LRUMap<BatchPersistencePackage, BatchDynamicResultSet>(1000));

    @Resource(name="ucExploitProtectionService")
    protected ExploitProtectionService exploitProtectionService;

    @Resource(name="ucPersistenceService")
    protected PersistenceService persistenceService;

    @Resource(name="ucPersistenceThreadManager")
    protected PersistenceThreadManager persistenceThreadManager;

    @Resource(name="ucStreamingTransactionCapableUtil")
    protected StreamingTransactionCapableUtil transUtil;

    protected ServiceException recreateSpecificServiceException(ServiceException e, String message, Throwable cause) {
        try {
            ServiceException newException;
            if (cause == null) {
                Constructor constructor = e.getClass().getConstructor(String.class);
                newException = (ServiceException) constructor.newInstance(message);
            } else {
                Constructor constructor = e.getClass().getConstructor(String.class, Throwable.class);
                newException = (ServiceException) constructor.newInstance(message, cause);
            }

            return newException;
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }

    @Override
    public PersistenceResponse inspect(final PersistencePackage persistencePackage) throws ServiceException {
        final PersistenceResponse[] response = new PersistenceResponse[1];
        try {
            PlatformTransactionManager transactionManager = identifyTransactionManager(persistencePackage);
            transUtil.runOptionalTransactionalOperation(new StreamCapableTransactionalOperationAdapter() {
                @Override
                public void execute() throws Throwable {
                    response[0] = nonTransactionalInspect(persistencePackage);
                }
            }, RuntimeException.class, true, TransactionDefinition.PROPAGATION_REQUIRED,
                    TransactionDefinition.ISOLATION_DEFAULT, true, transactionManager);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof ServiceException) {
                throw (ServiceException) e.getCause();
            }
            throw e;
        }
        return response[0];
    }

    @Override
    public PersistenceResponse nonTransactionalInspect(final PersistencePackage persistencePackage) throws ServiceException {
        return persistenceThreadManager.operation(TargetModeType.SANDBOX, persistencePackage, new Persistable <PersistenceResponse, ServiceException>() {
            @Override
            public PersistenceResponse execute() throws ServiceException {
                String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
                try {
                    PersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
                    return persistenceManager.inspect(persistencePackage);
                } catch (ServiceException e) {
                    String message = exploitProtectionService.cleanString(e.getMessage());
                    throw recreateSpecificServiceException(e, message, e.getCause());
                } catch (Exception e) {
                    LOG.error("Problem inspecting results for " + ceilingEntityFullyQualifiedClassname, e);
                    throw new ServiceException(exploitProtectionService.cleanString("Unable to fetch results for " + ceilingEntityFullyQualifiedClassname), e);
                }
            }
        });
    }

    @Override
    public PersistenceResponse fetch(final PersistencePackage persistencePackage, final CriteriaTransferObject cto) throws ServiceException {
        final PersistenceResponse[] response = new PersistenceResponse[1];
        try {
            PlatformTransactionManager transactionManager = identifyTransactionManager(persistencePackage);
            transUtil.runOptionalTransactionalOperation(new StreamCapableTransactionalOperationAdapter() {
                @Override
                public void execute() throws Throwable {
                    response[0] = nonTransactionalFetch(persistencePackage, cto);
                }
            }, RuntimeException.class, true, TransactionDefinition.PROPAGATION_REQUIRED,
                    TransactionDefinition.ISOLATION_DEFAULT, true, transactionManager);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof ServiceException) {
                throw (ServiceException) e.getCause();
            }
            throw e;
        }
        return response[0];
    }

    @Override
    public PersistenceResponse nonTransactionalFetch(final PersistencePackage persistencePackage, final CriteriaTransferObject cto) throws ServiceException {
        return persistenceThreadManager.operation(TargetModeType.SANDBOX, persistencePackage, new Persistable<PersistenceResponse, ServiceException>() {
            @Override
            public PersistenceResponse execute() throws ServiceException {
                try {
                    PersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
                    return persistenceManager.fetch(persistencePackage, cto);
                } catch (ServiceException e) {
                    LOG.error("Problem fetching results for " + persistencePackage.getCeilingEntityFullyQualifiedClassname(), e);
                    String message = exploitProtectionService.cleanString(e.getMessage());
                    throw recreateSpecificServiceException(e, message, e.getCause());
                }
            }
        });
    }

    protected void cleanEntity(Entity entity) throws ServiceException {
        Property currentProperty = null;
        for (Property property : entity.getProperties()) {
            try {
                currentProperty = property;
                property.setRawValue(property.getValue());
                property.setValue(exploitProtectionService.cleanStringWithResults(property.getValue()));
                property.setUnHtmlEncodedValue(StringEscapeUtils.unescapeHtml(property.getValue()));
            } catch (CleanStringException e) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < e.getCleanResults().getNumberOfErrors(); j++) {
                    sb.append("\n");
                    sb.append(j + 1);
                    sb.append(") ");
                    sb.append((String) e.getCleanResults().getErrorMessages().get(j));
                    sb.append("\n");
                }
                sb.append("\nNote - Antisamy policy in effect. Set a new policy file to modify validation behavior/strictness.");
                entity.addValidationError(currentProperty.getName(), sb.toString());
            }
        }
    }

    @Override
    public PersistenceResponse add(final PersistencePackage persistencePackage) throws ServiceException {
        final PersistenceResponse[] response = new PersistenceResponse[1];
        try {
            PlatformTransactionManager transactionManager = identifyTransactionManager(persistencePackage);
            transUtil.runTransactionalOperation(new StreamCapableTransactionalOperationAdapter() {
                @Override
                public void execute() throws Throwable {
                    response[0] = nonTransactionalAdd(persistencePackage);
                }

                @Override
                public boolean shouldRetryOnTransactionLockAcquisitionFailure() {
                    return super.shouldRetryOnTransactionLockAcquisitionFailure();
                }
            }, RuntimeException.class, transactionManager);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof ServiceException) {
                throw (ServiceException) e.getCause();
            }
            throw e;
        }
        return response[0];
    }

    @Override
    public PersistenceResponse update(final PersistencePackage persistencePackage) throws ServiceException {
        final PersistenceResponse[] response = new PersistenceResponse[1];
        try {
            PlatformTransactionManager transactionManager = identifyTransactionManager(persistencePackage);
            transUtil.runTransactionalOperation(new StreamCapableTransactionalOperationAdapter() {
                @Override
                public void execute() throws Throwable {
                    response[0] = nonTransactionalUpdate(persistencePackage);
                }

                @Override
                public boolean shouldRetryOnTransactionLockAcquisitionFailure() {
                    return super.shouldRetryOnTransactionLockAcquisitionFailure();
                }
            }, RuntimeException.class, transactionManager);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof ServiceException) {
                throw (ServiceException) e.getCause();
            }
            throw e;
        }
        return response[0];
    }

    @Override
    public PersistenceResponse remove(final PersistencePackage persistencePackage) throws ServiceException {
        final PersistenceResponse[] response = new PersistenceResponse[1];
        try {
            PlatformTransactionManager transactionManager = identifyTransactionManager(persistencePackage);
            transUtil.runTransactionalOperation(new StreamCapableTransactionalOperationAdapter() {
                @Override
                public void execute() throws Throwable {
                    response[0] = nonTransactionalRemove(persistencePackage);
                }

                @Override
                public boolean shouldRetryOnTransactionLockAcquisitionFailure() {
                    return super.shouldRetryOnTransactionLockAcquisitionFailure();
                }
            }, RuntimeException.class, transactionManager);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof ServiceException) {
                throw (ServiceException) e.getCause();
            }
            throw e;
        }
        return response[0];
    }

    @Override
    public PersistenceResponse nonTransactionalAdd(final PersistencePackage persistencePackage) throws ServiceException {
        return persistenceThreadManager.operation(TargetModeType.SANDBOX, persistencePackage, new Persistable <PersistenceResponse, ServiceException>() {
            @Override
            public PersistenceResponse execute() throws ServiceException {
                cleanEntity(persistencePackage.getEntity());
                try {
                    PersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
                    return persistenceManager.add(persistencePackage);
                } catch (ServiceException e) {
                    //immediately throw validation exceptions without printing a stack trace
                    if (e instanceof ValidationException) {
                        throw e;
                    } else if (e.getCause() instanceof ValidationException) {
                        throw (ValidationException) e.getCause();
                    }
                    String message = exploitProtectionService.cleanString(e.getMessage());
                    throw recreateSpecificServiceException(e, message, e.getCause());
                }
            }
        });
    }

    @Override
    public PersistenceResponse nonTransactionalUpdate(final PersistencePackage persistencePackage) throws ServiceException {
        return persistenceThreadManager.operation(TargetModeType.SANDBOX, persistencePackage, new Persistable <PersistenceResponse, ServiceException>() {
            @Override
            public PersistenceResponse execute() throws ServiceException {
                cleanEntity(persistencePackage.getEntity());
                try {
                    PersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
                    return persistenceManager.update(persistencePackage);
                } catch (ServiceException e) {
                    //immediately throw validation exceptions without printing a stack trace
                    if (e instanceof ValidationException) {
                        throw e;
                    } else if (e.getCause() instanceof ValidationException) {
                        throw (ValidationException) e.getCause();
                    }
                    LOG.error("Problem updating " + persistencePackage.getCeilingEntityFullyQualifiedClassname(), e);
                    String message = exploitProtectionService.cleanString(e.getMessage());
                    throw recreateSpecificServiceException(e, message, e.getCause());
                }
            }
        });
    }

    @Override
    public PersistenceResponse nonTransactionalRemove(final PersistencePackage persistencePackage) throws ServiceException {
        return persistenceThreadManager.operation(TargetModeType.SANDBOX, persistencePackage, new Persistable <PersistenceResponse, ServiceException>() {
            @Override
            public PersistenceResponse execute() throws ServiceException {
                try {
                    PersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
                    return persistenceManager.remove(persistencePackage);
                } catch (ServiceException e) {
                    //immediately throw validation exceptions without printing a stack trace
                    if (e instanceof ValidationException) {
                        throw e;
                    } else if (e.getCause() instanceof ValidationException) {
                        throw (ValidationException) e.getCause();
                    }
                    LOG.error("Problem removing " + persistencePackage.getCeilingEntityFullyQualifiedClassname(), e);
                    String message = exploitProtectionService.cleanString(e.getMessage());
                    throw recreateSpecificServiceException(e, message, e.getCause());
                }
            }
        });
    }

    protected PlatformTransactionManager identifyTransactionManager(PersistencePackage persistencePackage) throws ServiceException {
        String className = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        return persistenceService.identifyTransactionManager(className, TargetModeType.SANDBOX);
    }
}
