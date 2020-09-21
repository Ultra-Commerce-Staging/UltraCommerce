/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.search.dao;

import com.ultracommerce.core.search.domain.Field;
import com.ultracommerce.core.search.domain.FieldEntity;

import java.util.List;

/**
 * DAO to facilitate interaction with Ultra fields.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public interface FieldDao {

    /**
     * Given an abbreviation, returns the Field object that maps to this abbreviation.
     * Note that the default Ultra implementation of Field will enforce a uniqueness
     * constraint on the abbreviation field and this method will reliably return one field
     * 
     * @param abbreviation
     * @return the Field that has this abbreviation
     */
    public Field readFieldByAbbreviation(String abbreviation);

    List<Field> readAllProductFields();

    List<Field> readAllSkuFields();

    /**
     * Finds all fields based on the entity type.
     * @param entityType
     * @return
     */
    public List<Field> readFieldsByEntityType(FieldEntity entityType);

    /**
     * Persist an instance to the data layer.
     *
     * @param field the instance to persist
     * @return the instance after it has been persisted
     */
    public Field save(Field field);
}
