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
package com.ultracommerce.common.extensibility.jpa.copy;

import com.ultracommerce.common.extensibility.jpa.convert.UltraClassTransformer;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Properties;

/**
 * This class transformer will do nothing. The main use case for this transformer is when you would prefer to not
 * have a module's template classes copied over, and would rather do it yourself. 
 * 
 * This transformer should not typically be used.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class NullClassTransformer implements UltraClassTransformer {
    
    @Override
    public void compileJPAProperties(Properties props, Object key) throws Exception {
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, 
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return null;
    }

}

