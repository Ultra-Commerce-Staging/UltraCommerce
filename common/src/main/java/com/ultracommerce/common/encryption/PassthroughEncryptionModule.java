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
package com.ultracommerce.common.encryption;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * The default encryption module simply passes through the decrypt and encrypt text.
 * A real implementation should adhere to PCI compliance, which requires robust key
 * management, including regular key rotation. An excellent solution would be a module
 * for interacting with the StrongKey solution. Refer to this discussion:
 *
 * http://www.strongauth.com/forum/index.php?topic=44.0
 *
 * @author jfischer
 *
 */
public class PassthroughEncryptionModule implements EncryptionModule {

    private static final Log LOG = LogFactory.getLog(PassthroughEncryptionModule.class);

    @Autowired
    protected Environment env;

    @PostConstruct
    public void init() {
        if (env.acceptsProfiles("production")) {
            LOG.warn("This passthrough encryption module provides NO ENCRYPTION and should NOT be used in production.");
        }
    }

    @Override
    public String decrypt(String cipherText) {
        return cipherText;
    }

    @Override
    public String encrypt(String plainText) {
        return plainText;
    }

    @Override
    public Boolean matches(String raw, String encrypted) {
        return encrypted.equals(encrypt(raw));
    }
}
