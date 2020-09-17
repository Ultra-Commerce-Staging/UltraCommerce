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
package com.ultracommerce.openadmin.server.service.persistence.validation;

import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.ValidationConfiguration;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

/**
 * <p>Implementation of the {@link EntityValidatorService} that runs entities through JSR-303 validations. The default
 * behavior of this implementation is to use the out-of-the-box Ultra validations (through {@link ValidationConfiguration}
 * <i>in addition to</i> any JSR-303 annotations that you have configured on your entity.</p>
 * 
 * <p>In order to use this validator rather than the default, you will need to include an implementation of {@link Validator}
 * in your Spring root application context (not the servlet). For example, you would modify your applicationContext-admin.xml
 * to inject Spring's default implementation of {@link Validator} (the one used by Spring MVC):</p>
 * <code>
 * <pre>
 * &lt;bean class=&quotorg.springframework.validation.beanvalidation.LocalValidatorFactoryBean&quot/&gt;
 * </pre>
 * </code>
 * Then override the the ucEntityValidatorService bean to use this class instead:
 * <code>
 * <pre>
 * &lt;bean id=&quotucEntityValidatorService&quot 
 *        class=&quotcom.ultracommerce.openadmin.server.service.persistence.validation.BeanValidationEntityValidatorServiceImpl&quot/&gt;
 * </pre>
 * </code>
 * 
 * <p>For more information on the default Spring JSR-303 validator, check out the docs at
 * 
 * 
 * @author Phillip Verheyden
 * @see {@link EntityValidatorServiceImpl#validate(Entity, Serializable, Map)}
 * @see {@link Validator}
 * @see <a href="http://static.springsource.org/spring/docs/3.1.3.RELEASE/spring-framework-reference/html/validation.html#validation-beanvalidation">Spring Validation Docs</a>
 */
public class BeanValidationEntityValidatorServiceImpl extends EntityValidatorServiceImpl {

    @Autowired
    protected Validator validator;
    
    /**
     * If true (default behavior) this will invoke the default implementation to perform validations hooked up via
     * {@link ValidationConfiguration} from {@link AdminPresentation}.
     */
    protected boolean useDefaultEntityValidations = true;
    
    @Override
    public void validate(Entity entity, Serializable instance, Map<String, FieldMetadata> mergedProperties,
            RecordHelper recordHelper, boolean validateUnsubmittedProperties) {        
        if (isUseDefaultEntityValidations()) {
            super.validate(entity, instance, mergedProperties, recordHelper, validateUnsubmittedProperties);
        }

        Set<ConstraintViolation<Serializable>> violations = getValidator().validate(instance);
        for (ConstraintViolation<Serializable> violation : violations) {
            entity.addValidationError(violation.getPropertyPath().toString(), violation.getMessage());
        }
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public boolean isUseDefaultEntityValidations() {
        return useDefaultEntityValidations;
    }

    public void setUseDefaultEntityValidations(boolean useDefaultEntityValidations) {
        this.useDefaultEntityValidations = useDefaultEntityValidations;
    }

}
