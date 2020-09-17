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
package com.ultracommerce.common.email.service.info;

import java.io.IOException;

/**
 * Implementation of EmailInfo that will not send an Email.   The out of box configuration for
 * ultra does not send emails but does have hooks to send emails for use cases like
 * registration, forgot password, etc.
 *
 * The email send functionality will not send an email if the passed in EmailInfo is an instance
 * of this class.
 *
 * @author vjain
 *
 */
public class NullEmailInfo extends EmailInfo {
    private static final long serialVersionUID = 1L;

    public NullEmailInfo() throws IOException {
        super();
    }

}
