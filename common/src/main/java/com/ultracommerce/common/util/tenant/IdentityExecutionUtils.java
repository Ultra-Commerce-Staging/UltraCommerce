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
package com.ultracommerce.common.util.tenant;

import com.ultracommerce.common.site.domain.Catalog;
import com.ultracommerce.common.site.domain.Site;
import com.ultracommerce.common.util.TransactionUtils;
import com.ultracommerce.common.web.UltraRequestContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * The utility methods in this class provide a way to ignore the currently configured site/catalog contexts and instead
 * explicitly run operations in the specified context.
 * 
 * @author Jeff Fischer
 */
public class IdentityExecutionUtils {

    public static <T, G extends Throwable> T runOperationByIdentifier(IdentityOperation<T, G> operation, Site site, Site profile, Catalog catalog,
                                                              PlatformTransactionManager transactionManager) throws G {
        IdentityUtilContext context = new IdentityUtilContext();
        context.setIdentifier(site);
        IdentityUtilContext.setUtilContext(context);
        TransactionContainer container = null;
        boolean isError = false;
        Site previousSite = null;
        Catalog previousCatalog = null;
        Site previousProfile = null;
        Boolean previousIsIgnoringSite = false;

        UltraRequestContext originalBrc = UltraRequestContext.getUltraRequestContext(false);
        if (originalBrc != null) {
            previousSite = originalBrc.getNonPersistentSite();
            previousCatalog = originalBrc.getCurrentCatalog();
            previousProfile = originalBrc.getCurrentProfile();
            previousIsIgnoringSite = originalBrc.getIgnoreSite();
        }
        
        boolean isNew = initRequestContext(site, profile, catalog);

        try {
            if (transactionManager != null) {
                container = establishTransaction(transactionManager);
            }

            try {
                return operation.execute();
            } catch (RuntimeException e) {
                isError = true;
                throw e;
            }
        } finally {
            try {
                if (container != null) {
                    finalizeTransaction(transactionManager, container, isError);
                }
            } finally {
                IdentityUtilContext.setUtilContext(null);
                if (isNew) {
                    UltraRequestContext.setUltraRequestContext(null);
                } else {
                    UltraRequestContext.getUltraRequestContext().setIgnoreSite(previousIsIgnoringSite);
                    UltraRequestContext.getUltraRequestContext().setNonPersistentSite(previousSite);
                    UltraRequestContext.getUltraRequestContext().setCurrentCatalog(previousCatalog);
                    UltraRequestContext.getUltraRequestContext().setCurrentProfile(previousProfile);
                }
            }
        }
    }

    public static <T, G extends Throwable> T runOperationByIdentifier(IdentityOperation<T, G> operation, Site site, Catalog catalog) throws G {
        return runOperationByIdentifier(operation, site, null, catalog, null);
    }

    public static <T, G extends Throwable> T runOperationByIdentifier(IdentityOperation<T, G> operation, Site site, Site profile, Catalog catalog) throws G {
        return runOperationByIdentifier(operation, site, profile, catalog, null);
    }

    public static <T, G extends Throwable> T runOperationByIdentifier(IdentityOperation<T, G> operation, Site site) throws G {
        return runOperationByIdentifier(operation, site, null, null, null);
    }

    public static <T, G extends Throwable> T runOperationByIdentifier(IdentityOperation<T, G> operation, Site site, Site profile) throws G {
        return runOperationByIdentifier(operation, site, profile, null);
    }

    public static <T, G extends Throwable> T runOperationAndIgnoreIdentifier(IdentityOperation<T, G> operation) throws G {
        return runOperationAndIgnoreIdentifier(operation, null);
    }
    
    public static <T, G extends Throwable> T runOperationAndIgnoreIdentifier(IdentityOperation<T, G> operation, 
            PlatformTransactionManager transactionManager) throws G {
        //Set up pre-existing state...
        UltraRequestContext brc = UltraRequestContext.getUltraRequestContext(false);
        Site previousSite = null;
        Catalog previousCatalog = null;
        Site previousProfile = null;
        Boolean previousIsIgnoringSite = false;

        if (brc != null) {
            previousSite = brc.getNonPersistentSite();
            previousCatalog = brc.getCurrentCatalog();
            previousProfile = brc.getCurrentProfile();
            previousIsIgnoringSite = brc.getIgnoreSite();
        }

        //Initialize new state...  This will guaratee that a UltraRequestContext is available to this thread.
        boolean isNew = initRequestContext(null, null, null);

        TransactionContainer container = null;

        boolean isError = false;
        try {
            UltraRequestContext.getUltraRequestContext().setIgnoreSite(true);

            if (transactionManager != null) {
                container = establishTransaction(transactionManager);
            }

            return operation.execute();
        } catch (RuntimeException e) {
            isError = true;
            throw e;
        } finally {
            try {
                if (container != null) {
                    finalizeTransaction(transactionManager, container, isError);
                }
            } finally {
                if (isNew) {
                    //We just created this, so don't leave it lying around.
                    UltraRequestContext.setUltraRequestContext(null);
                } else {
                    //Otherwise, reset pre-existing state.
                    UltraRequestContext.getUltraRequestContext().setIgnoreSite(previousIsIgnoringSite);
                    UltraRequestContext.getUltraRequestContext().setNonPersistentSite(previousSite);
                    UltraRequestContext.getUltraRequestContext().setCurrentCatalog(previousCatalog);
                    UltraRequestContext.getUltraRequestContext().setCurrentProfile(previousProfile);
                }
            }
        }
    }

    private static boolean initRequestContext(Site site, Site profile, Catalog catalog) {
        boolean isNew = false;
        UltraRequestContext requestContext = UltraRequestContext.getUltraRequestContext(false);

        if (requestContext == null) {
            requestContext = new UltraRequestContext();
            UltraRequestContext.setUltraRequestContext(requestContext);
            isNew = true;
        }

        requestContext.setNonPersistentSite(site);
        requestContext.setCurrentCatalog(catalog);
        requestContext.setCurrentProfile(profile);
        
        if (site != null) {
            requestContext.setIgnoreSite(false);
        }

        return isNew;
    }

    private static void finalizeTransaction(PlatformTransactionManager transactionManager, TransactionContainer
            container, boolean error) {
        TransactionUtils.finalizeTransaction(container.status, transactionManager, error);
        for (Map.Entry<Object, Object> entry : container.usedResources.entrySet()) {
            if (!TransactionSynchronizationManager.hasResource(entry.getKey())) {
                TransactionSynchronizationManager.bindResource(entry.getKey(), entry.getValue());
            }
        }
    }

    private static TransactionContainer establishTransaction(PlatformTransactionManager transactionManager) {
        Map<Object, Object> usedResources = new HashMap<Object, Object>();
        Map<Object, Object> resources = TransactionSynchronizationManager.getResourceMap();
        for (Map.Entry<Object, Object> entry : resources.entrySet()) {
            if ((entry.getKey() instanceof EntityManagerFactory  || entry.getKey() instanceof DataSource) &&
                    TransactionSynchronizationManager.hasResource(entry.getKey())) {
                usedResources.put(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry<Object, Object> entry : usedResources.entrySet()) {
            TransactionSynchronizationManager.unbindResource(entry.getKey());
        }

        TransactionStatus status;
        try {
            status = TransactionUtils.createTransaction(TransactionDefinition.PROPAGATION_REQUIRES_NEW,
                    transactionManager, false);
        } catch (RuntimeException e) {
            throw e;
        }
        return new TransactionContainer(status, usedResources);
    }

    private static class TransactionContainer {
        TransactionStatus status;
        Map<Object, Object> usedResources;

        private TransactionContainer(TransactionStatus status, Map<Object, Object> usedResources) {
            this.status = status;
            this.usedResources = usedResources;
        }

        public TransactionStatus getStatus() {
            return status;
        }

        public void setStatus(TransactionStatus status) {
            this.status = status;
        }

        public Map<Object, Object> getUsedResources() {
            return usedResources;
        }

        public void setUsedResources(Map<Object, Object> usedResources) {
            this.usedResources = usedResources;
        }
    }
}
