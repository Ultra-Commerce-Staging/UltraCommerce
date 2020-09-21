/*
 * #%L
 * UltraCommerce Framework
 * %%
 * Copyright (C) 2009 - 2019 Ultra Commerce
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
package com.ultracommerce.core.search.service.solr.indexer;

import org.apache.solr.common.SolrInputDocument;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.locale.domain.Locale;
import com.ultracommerce.core.catalog.domain.Indexable;
import com.ultracommerce.core.search.domain.IndexField;

import java.util.List;

/**
 * This is the entry point for issuing commands to update Solr indexes / collections.
 * 
 * @author Kelly Tisdell
 *
 */
public interface SolrIndexUpdateService {
    
    /**
     * Default behavior is to truncate the background collection (offline index), populate it with documents, 
     * commit, and then swap (re-alias) so that it becomes the foreground collection and the foreground collection becomes the background.
     * 
     * @throws ServiceException
     */
    public void rebuildIndex() throws ServiceException;
    
    /**
     * This is for relatively small, autonomous, incremental updates to the main (customer-facing) foreground Solr collection / index.  
     * 
     * Writes the documents to the foreground collection / index.  Then issues a commit.
     * 
     * This should not be used for incremental indexing in the context of a larger indexing scope or operation.
     * 
     * @param documents
     * @throws ServiceException
     */
    public void updateIndex(List<SolrInputDocument> documents) throws ServiceException;
    
    /**
     * This is for relatively small, autonomous, incremental updates to the main (customer-facing) foreground Solr collection / index.  
     * 
     * Executes the delete queries, if any, in the foreground index.  Then writes the documents, if any, to the foreground index.
     * Then commits if no errors occur.
     * 
     * This should not be used for incremental indexing in the context of a larger indexing scope or operation.
     * 
     * @param documents
     * @param deleteQueries
     * @throws ServiceException
     */
    public void updateIndex(List<SolrInputDocument> documents, List<String> deleteQueries) throws ServiceException;
    
    /**
     * Provides an interface for a caller to convert an {@link Indexable} into a {@link SolrInputDocument}. This may return null if the implementor does not want 
     * the specified {@link Indexable} indexed.
     * 
     * @param indexable
     * @return
     */
    public SolrInputDocument buildDocument(Indexable indexable);
    
    /**
     * Provides an interface for a caller to convert an {@link Indexable} into a {@link SolrInputDocument}. This may return null if the implementor does not want 
     * the specified {@link Indexable} indexed.
     * 
     * @param indexable
     * @param fields
     * @param locales
     * @return
     */
    public SolrInputDocument buildDocument(Indexable indexable, List<IndexField> fields, List<Locale> locales);
}
