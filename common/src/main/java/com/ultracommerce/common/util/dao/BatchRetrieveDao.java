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
package com.ultracommerce.common.util.dao;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author jfischer
 *
 */
public class BatchRetrieveDao {
    
    //Default batch read size
    private int inClauseBatchSize = 300;
    
    @SuppressWarnings("unchecked")
    public <T> List<T> batchExecuteReadQuery(Query query, List<?> params, String parameterName) {
        List<T> response = new ArrayList<T>();
        int start = 0;
        while (start < params.size()) {
            List<?> batchParams = params.subList(start, params.size() < inClauseBatchSize ? params.size() : inClauseBatchSize);
            query.setParameter(parameterName, batchParams);
            response.addAll(query.getResultList());
            start += inClauseBatchSize;
        }
        return response;
    }

    public int getInClauseBatchSize() {
        return inClauseBatchSize;
    }

    public void setInClauseBatchSize(int inClauseBatchSize) {
        this.inClauseBatchSize = inClauseBatchSize;
    }
    
}
