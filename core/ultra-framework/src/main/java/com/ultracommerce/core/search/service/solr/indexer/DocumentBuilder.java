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
import com.ultracommerce.common.locale.domain.Locale;
import com.ultracommerce.core.catalog.domain.Indexable;
import com.ultracommerce.core.search.domain.IndexField;

import java.util.List;

/**
 * Component to build a Solr document from an {@link Indexable}.
 * 
 * @author Kelly Tisdell
 *
 */
public interface DocumentBuilder {

    public SolrInputDocument buildDocument(Indexable indexable, List<IndexField> fields, List<Locale> locales);
    
}
