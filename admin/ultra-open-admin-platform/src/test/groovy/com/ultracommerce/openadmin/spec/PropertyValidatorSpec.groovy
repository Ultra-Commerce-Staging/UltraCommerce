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
package com.ultracommerce.openadmin.spec

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

import com.ultracommerce.openadmin.dto.BasicFieldMetadata
import com.ultracommerce.openadmin.server.service.persistence.validation.RequiredPropertyValidator

import spock.lang.Specification


/**
 * 
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
class PropertyValidatorSpec extends Specification {
    
    RequiredPropertyValidator validator
    BasicFieldMetadata md
    
    def setup() {
        validator = new RequiredPropertyValidator()
        md = new BasicFieldMetadata()
    }
    
    def "null values are not valid when required"() {
        when:
        md.required = true
            
        then:
        validator.validate(null, null, null, md, null, null).isNotValid()
    }
    
    def "empty strings are not valid when required"() {
        when:
        md.required = true
            
        then:
        validator.validate(null, null, null, md, null, "").isNotValid()
    }
    
    def "any values are valid when required"() {
        when:
        md.required = true
            
        then:
        validator.validate(null, null, null, md, null, "testest").isValid()
        validator.validate(null, null, null, md, null, "1.8832").isValid()
    }
    
    def "non-required metadata should allos nulls and empty strings"() {
        when:
        md.required = false
            
        then:
        validator.validate(null, null, null, md, null, null).isValid()
        validator.validate(null, null, null, md, null, "").isValid()
    }
    
    def "required overrides should always take precedence"() {
        when: "required is true"
        md.required = true
            
        and: "required override is false"
        md.requiredOverride = false
            
        then: "empty strings and nulls and values should be allowed"
        validator.validate(null, null, null, md, null, null).isValid()
        validator.validate(null, null, null, md, null, "").isValid()
        validator.validate(null, null, null, md, null, "testtest").isValid()
            
        when: "required is false"
        md.required = false
            
        and: "required override is true"
        md.requiredOverride = true
            
        then: "empty strings and null values should not be allowed"
        validator.validate(null, null, null, md, null, null).isNotValid()
        validator.validate(null, null, null, md, null, "").isNotValid()
        validator.validate(null, null, null, md, null, "testtest").isValid()
    }
    
}
