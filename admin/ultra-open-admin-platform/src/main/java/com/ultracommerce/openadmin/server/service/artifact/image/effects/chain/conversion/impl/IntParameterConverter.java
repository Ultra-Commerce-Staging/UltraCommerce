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
package com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.conversion.impl;

import com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.conversion.ConversionException;
import com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.conversion.Parameter;
import com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.conversion.ParameterConverter;

public class IntParameterConverter implements ParameterConverter {

    /* (non-Javadoc)
     * @see com.xpressdocs.email.asset.effects.chain.conversion.ParameterConverter#convert(java.lang.String, double)
     */
    public Parameter convert(String value, Double factor, boolean applyFactor) throws ConversionException {
        Parameter param = new Parameter();
        param.setParameterClass(int.class);
        param.setParameterInstance((int) (applyFactor&&factor!=null?Integer.parseInt(value)/factor:Integer.parseInt(value)));
        
        return param;
    }

}
