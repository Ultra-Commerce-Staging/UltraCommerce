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
package com.ultracommerce.openadmin.server.service.artifact.image.effects.chain;

import com.ultracommerce.openadmin.server.service.artifact.OperationBuilder;
import com.ultracommerce.openadmin.server.service.artifact.image.Operation;
import com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.conversion.ConversionManager;
import com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.conversion.Parameter;
import com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.filter.AlterHSB;
import com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.filter.AlterRGB;
import com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.filter.AutoLevelsRGB;
import com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.filter.Crop;
import com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.filter.FilterTypeEnum;
import com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.filter.GaussianBlur;
import com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.filter.Resize;
import com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.filter.Rotate;
import com.ultracommerce.openadmin.server.service.artifact.image.effects.chain.filter.UnsharpMask;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ucImageEffectsManager")
public class EffectsManager {

    @Resource(name="ucImageConversionManager")
    protected ConversionManager conversionManager;
    protected Map<String, OperationBuilder> filters = new HashMap<String, OperationBuilder>();

    public EffectsManager() {
        filters.put(FilterTypeEnum.ALTERHSB.toString().toLowerCase(), new AlterHSB());
        filters.put(FilterTypeEnum.ALTERRGB.toString().toLowerCase(), new AlterRGB());
        filters.put(FilterTypeEnum.AUTOLEVELSRGB.toString().toLowerCase(), new AutoLevelsRGB());
        filters.put(FilterTypeEnum.CROP.toString().toLowerCase(), new Crop());
        filters.put(FilterTypeEnum.GAUSSIANBLUR.toString().toLowerCase(), new GaussianBlur());
        filters.put(FilterTypeEnum.RESIZE.toString().toLowerCase(), new Resize());
        filters.put(FilterTypeEnum.ROTATE.toString().toLowerCase(), new Rotate());
        filters.put(FilterTypeEnum.UNSHARPMASK.toString().toLowerCase(), new UnsharpMask());
    }

    public Operation[] buildOperations(Map<String, String> parameterMap, InputStream artifactStream, String mimeType) {
        List<Operation> operations = new ArrayList<Operation>();
        for (OperationBuilder builder : filters.values()) {
            Operation operation = builder.buildOperation(parameterMap, artifactStream, mimeType);
            if (operation != null) {
                operations.add(operation);
            }
        }

        return operations.toArray(new Operation[]{});
    }

    public BufferedImage renderEffect(String effectName, Double factor, UnmarshalledParameter[] parameters, BufferedImage src) throws Exception {
        /*
         * retrieve the injected filter, instantiate the filter instance using reflection and execute the operation
         */
        Object filterObject = filters.get(effectName);
        if (filterObject == null) {
            throw new FilterNotFoundException("An effects filter was not found for the name: " + effectName);
        }
        Class filterClass = filterObject.getClass();

        Class[] types = new Class[1];
        Object[] args = new Object[1];
        if (parameters != null) {
            Parameter[] marshalledParameters = new Parameter[parameters.length];
            for (int j=0;j<parameters.length;j++) {
                marshalledParameters[j] = conversionManager.convertParameter(parameters[j].getValue(), parameters[j].getType(), factor, parameters[j].isApplyFactor());
            }

            types = new Class[marshalledParameters.length + 1];
            args = new Object[marshalledParameters.length + 1];
            for (int j=0;j<types.length-1;j++){
                types[j] = marshalledParameters[j].getParameterClass();
                args[j] = marshalledParameters[j].getParameterInstance();
            }
        }
        types[types.length-1] = RenderingHints.class;
        args[types.length-1] = null;
        Constructor constructor = filterClass.getConstructor(types);
        Object filterInstance = constructor.newInstance(args);
        
        Method filterMethod = filterClass.getMethod("filter", new Class[]{BufferedImage.class, BufferedImage.class});
        Object result = filterMethod.invoke(filterInstance, new Object[]{src, null});
        
        return (BufferedImage) result;
    }

    /**
     * @return the filters
     */
    public Map<String, OperationBuilder> getFilters() {
        return filters;
    }

    /**
     * @param filters the filters to set
     */
    public void setFilters(Map<String, OperationBuilder> filters) {
        this.filters.putAll(filters);
    }

    /**
     * @return the parameterConverter
     */
    public ConversionManager getConversionManager() {
        return conversionManager;
    }

    /**
     * @param parameterConverter the parameterConverter to set
     */
    public void setConversionManager(ConversionManager conversionManager) {
        this.conversionManager = conversionManager;
    }
}
