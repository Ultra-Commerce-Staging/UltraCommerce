/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.server.service.persistence.module;

import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.server.service.handler.CustomPersistenceHandler;

import java.io.Serializable;
import java.util.List;

/**
 * Wrap calls to {@link BasicPersistenceModule#getPersistentRecords(String, List, Integer, Integer)},
 * {@link BasicPersistenceModule#getTotalRecords(String, List)}, and {@link BasicPersistenceModule#getRecords(FetchExtractionRequest)},
 * presumably to provide additional configuration or state modification before calling those methods on BasicPersistenceModule.
 *
 * @author Jeff Fischer
 */
public interface FetchWrapper {

    /**
     * Retrieve the records from the persistent store given the params in the {@link FetchRequest}. Presumably, modifications
     * or state manipulation may be performed to affect an alteration of the resulting SQL select syntax.
     *
     * @param fetchRequest
     * @return
     */
    List<Serializable> getPersistentRecords(FetchRequest fetchRequest);

    /**
     * Retrieve the total record count given the params in the {@link FetchRequest}. Presumably, modifications or
     * state manipulation may be performed to affect an alteration of the resulting SQL select count syntax.
     *
     * @param fetchRequest
     * @return
     */
    Integer getTotalRecords(FetchRequest fetchRequest);

    /**
     * Retrieve the dto representation of persistent records based on the record contents and entity metadata.
     *
     * @param fetchExtractionRequest
     * @return
     */
    Entity[] getRecords(FetchExtractionRequest fetchExtractionRequest);

}
