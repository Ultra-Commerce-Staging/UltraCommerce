/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.common.util;

import java.util.regex.Pattern;

/**
 * Simple bean that can represent a key/value pair used by arbitrary components for the purpose of engaging exclusion behavior.
 * Generally, there will be a component listening for IgnorableItem instances of a specific key and will use the values regex
 * to engage some exclusion behavior.
 *
 * @author Jeff Fischer
 */
public class IgnorableItem {

    private String key;
    private String value;
    private Pattern compiled;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Pattern getCompiled() {
        if (compiled == null) {
            compiled = Pattern.compile(value);
        }
        return compiled;
    }
}
