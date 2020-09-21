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
package com.ultracommerce.openadmin.web.rulebuilder.dto;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Elbert Bautista (elbertbautista)
 *
 * An example of the Serialized JSON:
 *
 * {'fields': [
 *      {'label':'Order Item - name',
 *      'id':'name',
 *      'operators':ucOperators_Text,
 *      'values':[]},
 *      {'label':'Order Item - Retail Price',
 *      'id':'retailPrice',
 *      'operators':ucOperators_Numeric,
 *      'values':[]},
 *      {'label':'Product - is Featured Product',
 *      'id':'sku.product.isFeaturedProduct',
 *      'operators':ucOperators_Boolean,
 *      'values':[]},
 *      {'label':'Sku - Active End Date',
 *      'id':'sku.activeEndDate',
 *      'operators':ucOperators_Date,
 *      'values':[]},
 *      {'label':'Category - Fulfillment Type',
 *      'id':'category.fulfillmentType',
 *      'operators':ucOperators_Enumeration,
 *      'values':ucOptions_FulfillmentType}
 * ]}
 *
 */
public class FieldWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    protected ArrayList<FieldDTO> fields = new ArrayList<FieldDTO>();

    public ArrayList<FieldDTO> getFields() {
        return fields;
    }

    public void setFields(ArrayList<FieldDTO> fields) {
        this.fields = fields;
    }
    
    public String serialize() throws JsonGenerationException, JsonMappingException, IOException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
