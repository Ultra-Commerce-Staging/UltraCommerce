/*
 * #%L
 * UltraCommerce Open Admin Platform
 * %%
 * Copyright (C) 2009 - 2019 Ultra Commerce
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
package com.ultracommerce.openadmin.server.service.persistence.validation;

import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * A Spring bean extending this class will automatically be called when validation is done on the entity specified by
 * the generic type.
 *
 * The persistent entity class that Hibernate is aware of should be used as the generic type. For example,
 * SomeEntityImpl instead of SomeEntity interface.
 * 
 * In the implementation of validate {@link Entity#addValidationError(String, String)} and
 * {@link Entity#addValidationError(String, String)} can be used to create an error that is displayed to the user before
 * an add or update occurs. {@link Entity#isValidationFailure()} can be used to see if the core validation found any
 * issues like required fields being blank to decide if any additional validation should be executed.
 * 
 * @param <T> Persistence Entity implementation to validate
 */
public abstract class UltraEntityValidator<T> {

	/**
	 * Validation that should be done on the specified entity after core validation is completed.
	 */
	public abstract void validate(Entity submittedEntity, @Nonnull T instance,
			Map<String, FieldMetadata> propertiesMetadata, RecordHelper recordHelper,
			boolean validateUnsubmittedProperties);

	@SuppressWarnings("unchecked")
	void validate(Entity submittedEntity, @Nonnull Serializable instance, Map<String, FieldMetadata> propertiesMetadata,
			RecordHelper recordHelper, boolean validateUnsubmittedProperties) {
		validate(submittedEntity, (T) instance, propertiesMetadata, recordHelper, validateUnsubmittedProperties);
	}
}
