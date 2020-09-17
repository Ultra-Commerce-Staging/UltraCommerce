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
package com.ultracommerce.common.enumeration.domain;

import com.ultracommerce.common.copy.CreateResponse;
import com.ultracommerce.common.copy.MultiTenantCopyContext;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import com.ultracommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.AdminPresentationCollection;
import com.ultracommerce.common.presentation.PopulateToOneFieldsEnum;
import com.ultracommerce.common.presentation.client.AddMethodType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="UC_DATA_DRVN_ENUM")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucStandardElements")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "DataDrivenEnumerationImpl_friendyName")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class DataDrivenEnumerationImpl implements DataDrivenEnumeration {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "DataDrivenEnumerationId")
    @GenericGenerator(
        name="DataDrivenEnumerationId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="DataDrivenEnumerationImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.common.enumeration.domain.DataDrivenEnumerationImpl")
        }
    )
    @Column(name = "ENUM_ID")
    protected Long id;
    
    @Column(name = "ENUM_KEY")
    @Index(name = "ENUM_KEY_INDEX", columnNames = {"KEY"})
    @AdminPresentation(friendlyName = "DataDrivenEnumerationImpl_Key", order = 1, gridOrder = 1, prominent = true)
    protected String key;
    
    @Column(name = "MODIFIABLE")
    @AdminPresentation(friendlyName = "DataDrivenEnumerationImpl_Modifiable", order = 2, gridOrder = 2, prominent = true)
    protected Boolean modifiable = false;

    @OneToMany(mappedBy = "type", targetEntity = DataDrivenEnumerationValueImpl.class, cascade = {CascadeType.ALL})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucStandardElements")
    @AdminPresentationCollection(addType = AddMethodType.PERSIST, friendlyName = "DataDrivenEnumerationImpl_Enum_Values", order = 3)
    protected List<DataDrivenEnumerationValue> enumValues = new ArrayList<DataDrivenEnumerationValue>();
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public Boolean getModifiable() {
        if (modifiable == null) {
            return Boolean.FALSE;
        } else {
            return modifiable;
        }
    }

    @Override
    public void setModifiable(Boolean modifiable) {
        this.modifiable = modifiable;
    }

    @Override
    public List<DataDrivenEnumerationValue> getEnumValues() {
        return enumValues;
    }

    @Override
    public void setEnumValues(List<DataDrivenEnumerationValue> enumValues) {
        this.enumValues = enumValues;
    }

    @Override
    @Deprecated
    public List<DataDrivenEnumerationValue> getOrderItems() {
        return enumValues;
    }

    @Override
    @Deprecated
    public void setOrderItems(List<DataDrivenEnumerationValue> orderItems) {
        this.enumValues = orderItems;
    }

    @Override
    public <G extends DataDrivenEnumeration> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context)
            throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        DataDrivenEnumeration cloned = createResponse.getClone();
        cloned.setKey(key);
        cloned.setModifiable(modifiable);
        for (DataDrivenEnumerationValue value : enumValues) {
            DataDrivenEnumerationValue clonedValue = value.createOrRetrieveCopyInstance(context).getClone();
            cloned.getEnumValues().add(clonedValue);
        }
        return createResponse;
    }

}
