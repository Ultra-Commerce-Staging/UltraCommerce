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
package com.ultracommerce.openadmin.server.service.persistence.module.criteria;

/**
 * Thrown when a problem converting a particular {@link com.ultracommerce.openadmin.server.service.persistence.module.criteria.FieldPath}
 * from a {@link com.ultracommerce.openadmin.server.service.persistence.module.criteria.FilterMapping} is detected
 * during JPA criteria translation for fetch operation.
 *
 * @see com.ultracommerce.openadmin.server.service.persistence.module.criteria.CriteriaTranslatorImpl
 * @author Jeff Fischer
 */
public class CriteriaConversionException extends RuntimeException {

    protected FieldPath fieldPath;

    public CriteriaConversionException(String message, FieldPath fieldPath) {
        super(message);
        this.fieldPath = fieldPath;
    }

    public FieldPath getFieldPath() {
        return fieldPath;
    }

    public void setFieldPath(FieldPath fieldPath) {
        this.fieldPath = fieldPath;
    }
}
