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
/**
 * 
 */
package com.ultracommerce.core.catalog.domain;

import com.ultracommerce.core.search.dao.FieldDao;
import com.ultracommerce.core.search.domain.Field;
import com.ultracommerce.core.search.domain.FieldEntity;
import com.ultracommerce.core.search.service.solr.SolrSearchServiceImpl;
import com.ultracommerce.core.search.service.solr.index.SolrIndexService;


/**
 * Mainly a marker interface denoting that the entity should be indexed for search
 * 
 * @see {@link SolrIndexService}
 * @see {@link SolrSearchServiceImpl}
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public interface Indexable {

    /**
     * The primary key for this indexable item that gets stored in the search index
     * 
     * {@see SolrHelperService#getIndexableIdFieldName()}
     */
    public Long getId();

    /**
     * Which type of {@link Field} should be queried for when looking up database-driven search fields to store in the
     * search index
     * 
     * @see SolrIndexService#buildIncrementalIndex(String, java.util.List, org.apache.solr.client.solrj.SolrClient)
     * @see FieldDao#readFieldsByEntityType(FieldEntity)
     */
    public FieldEntity getFieldEntityType();
    
}
