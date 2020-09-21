/*
 * #%L
 * UltraCommerce Advanced CMS
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
package com.ultracommerce.admin.server.service.handler;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.payment.PaymentAdditionalFieldType;
import com.ultracommerce.common.presentation.client.OperationType;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.common.presentation.client.VisibilityEnum;
import com.ultracommerce.openadmin.dto.BasicFieldMetadata;
import com.ultracommerce.openadmin.dto.ClassMetadata;
import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.DynamicResultSet;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.MergedPropertyType;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.dto.PersistencePerspective;
import com.ultracommerce.openadmin.dto.Property;
import com.ultracommerce.openadmin.server.dao.DynamicEntityDao;
import com.ultracommerce.openadmin.server.service.handler.ClassCustomPersistenceHandlerAdapter;
import com.ultracommerce.openadmin.server.service.persistence.module.InspectHelper;
import com.ultracommerce.openadmin.server.service.persistence.module.PersistenceModule;
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;
import com.ultracommerce.profile.core.domain.CustomerPayment;
import com.ultracommerce.profile.core.domain.CustomerPaymentImpl;
import com.ultracommerce.profile.core.service.CustomerPaymentService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 
 * @author Chris Kittrell (ckittrell)
 */
@Component("ucCustomerPaymentCustomPersistenceHandler")
public class CustomerPaymentCustomPersistenceHandler extends ClassCustomPersistenceHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(CustomerPaymentCustomPersistenceHandler.class);

    protected static final String SAVED_PAYMENT_INFO = "savedPaymentInfo";
    protected static final String NULL_LAST_FOUR = "****null";

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @Resource(name = "ucCustomerPaymentService")
    protected CustomerPaymentService customerPaymentService;

    public CustomerPaymentCustomPersistenceHandler() {
        super(CustomerPayment.class, CustomerPaymentImpl.class);
    }

    @Override
    public Boolean canHandleInspect(PersistencePackage pkg) {
        return classMatches(pkg) && isBasicOperation(pkg);
    }

    @Override
    public Boolean canHandleFetch(PersistencePackage pkg) {
        return classMatches(pkg) && isBasicOperation(pkg);
    }

    @Override
    public DynamicResultSet inspect(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao,
            InspectHelper helper) throws ServiceException {
        Map<MergedPropertyType, Map<String, FieldMetadata>> allMergedProperties = new HashMap<MergedPropertyType, Map<String, FieldMetadata>>();

        PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
        Map<String, FieldMetadata> properties = helper.getSimpleMergedProperties(CustomerPayment.class.getName(), persistencePerspective);

        // Hide "Payment Gateway Type" column & create "Saved Payment Info" ListGrid column
        FieldMetadata paymentGatewayType = properties.get("paymentGatewayType");
        if (paymentGatewayType != null) {
            ((BasicFieldMetadata) paymentGatewayType).setProminent(false);
        }

        BasicFieldMetadata savedPaymentInfo = new BasicFieldMetadata();
        savedPaymentInfo.setName(SAVED_PAYMENT_INFO);
        savedPaymentInfo.setFriendlyName("CustomerPaymentImpl_Saved_Payment_Info");
        savedPaymentInfo.setFieldType(SupportedFieldType.STRING);
        savedPaymentInfo.setInheritedFromType(CustomerPaymentImpl.class.getName());
        savedPaymentInfo.setAvailableToTypes(new String[]{CustomerPaymentImpl.class.getName()});
        savedPaymentInfo.setProminent(true);
        savedPaymentInfo.setGridOrder(2000);
        savedPaymentInfo.setReadOnly(true);
        savedPaymentInfo.setVisibility(VisibilityEnum.FORM_HIDDEN);
        properties.put(SAVED_PAYMENT_INFO, savedPaymentInfo);

        allMergedProperties.put(MergedPropertyType.PRIMARY, properties);
        Class<?>[] entityClasses = dynamicEntityDao.getAllPolymorphicEntitiesFromCeiling(CustomerPayment.class);
        ClassMetadata mergedMetadata = helper.buildClassMetadata(entityClasses, persistencePackage, allMergedProperties);

        return new DynamicResultSet(mergedMetadata, null, null);
    }

    @Override
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto,
            DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        OperationType fetchType = persistencePackage.getPersistencePerspective().getOperationTypes().getFetchType();
        PersistenceModule persistenceModule = helper.getCompatibleModule(fetchType);
        DynamicResultSet drs = persistenceModule.fetch(persistencePackage, cto);

        for (Entity entity : drs.getRecords()) {
            Property customerPaymentId = entity.findProperty("id");
            if (customerPaymentId != null) {
                CustomerPayment customerPayment = customerPaymentService.readCustomerPaymentById(Long.parseLong(customerPaymentId.getValue()));
                if (customerPayment != null) {
                    String savedPaymentDisplayValue = buildSavedPaymentDisplayValue(customerPayment);

                    Property derivedLabel = new Property();
                    derivedLabel.setName(SAVED_PAYMENT_INFO);
                    derivedLabel.setValue(savedPaymentDisplayValue);
                    entity.addProperty(derivedLabel);
                }
            }
        }

        return drs;
    }

    protected String buildSavedPaymentDisplayValue(CustomerPayment customerPayment) {
        String displayValue = new String();

        Map<String, String> fields = customerPayment.getAdditionalFields();
        if (MapUtils.isNotEmpty(fields)) {
            displayValue += buildPropertyValueIfAvailable(displayValue, fields.get(PaymentAdditionalFieldType.NAME_ON_CARD.getType()));
            displayValue += buildPropertyValueIfAvailable(displayValue, fields.get(PaymentAdditionalFieldType.CARD_TYPE.getType()));
            displayValue += buildPropertyValueIfAvailable(displayValue, fields.get(PaymentAdditionalFieldType.EXP_DATE.getType()));
            displayValue += buildPropertyValueIfAvailable(displayValue, "****" + fields.get(PaymentAdditionalFieldType.LAST_FOUR.getType()));
        }

        return displayValue;
    }

    protected String buildPropertyValueIfAvailable(String columnDisplayValue, String propertyValue) {
        if (propertyValue == null || NULL_LAST_FOUR.equals(propertyValue)) {
            return "";
        }

        return !columnDisplayValue.isEmpty() ? "\t|\t" + propertyValue : propertyValue;
    }

}
