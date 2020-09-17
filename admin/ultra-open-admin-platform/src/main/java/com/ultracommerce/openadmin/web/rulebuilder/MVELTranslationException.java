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
package com.ultracommerce.openadmin.web.rulebuilder;

import com.ultracommerce.common.exception.TranslatableException;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public class MVELTranslationException extends TranslatableException {

    private static final long serialVersionUID = 1L;

    public static final int SPECIFIED_FIELD_NOT_FOUND = 100;
    public static final int NO_FIELD_FOUND_IN_RULE = 101;
    public static final int INCOMPATIBLE_DATE_VALUE = 102;
    public static final int UNRECOGNIZABLE_RULE = 103;
    public static final int OPERATOR_NOT_FOUND = 104;
    public static final int INCOMPATIBLE_DECIMAL_VALUE = 105;
    public static final int INCOMPATIBLE_INTEGER_VALUE = 106;
    public static final int INCOMPATIBLE_RULE = 107;
    public static final int SUB_GROUP_DETECTED = 108;

    public MVELTranslationException(int code, String message) {
        super(code, message);
    }
}
