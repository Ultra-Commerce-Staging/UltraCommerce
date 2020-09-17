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
package com.ultracommerce.common.security.util;

import java.io.Serializable;

/**
 * 
 * @author jfischer
 *
 */
public class PasswordReset implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    private boolean passwordChangeRequired = false;
    private int passwordLength = 22;
    private boolean sendResetEmailReliableAsync = false;

    public PasswordReset() {
    }

    public PasswordReset(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public void setPasswordChangeRequired(boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
    }

    public boolean isSendResetEmailReliableAsync() {
        return sendResetEmailReliableAsync;
    }

    public void setSendResetEmailReliableAsync(boolean sendResetEmailReliableAsync) {
        this.sendResetEmailReliableAsync = sendResetEmailReliableAsync;
    }
}
