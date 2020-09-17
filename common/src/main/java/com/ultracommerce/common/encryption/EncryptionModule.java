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

/**
 * Basic extension point for modules handling encryption.
 */
public interface EncryptionModule {

    /**
     * Encrypt a text string
     * @param plainText
     * @return
     */
    public String encrypt(String plainText);

    /**
     * Decrypt a encrypted string
     * @param cipherText
     * @return
     */
    public String decrypt(String cipherText);

    /**
     * Check to see if a text string matches a generated encrypted token.
     * This is useful for encoders that always generate a unique hash.
     * @param raw
     * @param encrypted
     * @return
     */
    public Boolean matches(String raw, String encrypted);

}
