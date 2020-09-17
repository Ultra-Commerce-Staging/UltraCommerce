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
package com.ultracommerce.core.spec.search.service.solr

import org.apache.solr.client.solrj.SolrClient
import com.ultracommerce.common.locale.service.LocaleService
import com.ultracommerce.common.sandbox.SandBoxHelper
import com.ultracommerce.core.catalog.dao.ProductDao
import com.ultracommerce.core.catalog.dao.SkuDao
import com.ultracommerce.core.catalog.domain.ProductImpl
import com.ultracommerce.core.catalog.domain.Sku
import com.ultracommerce.core.catalog.domain.SkuImpl
import com.ultracommerce.core.search.dao.IndexFieldDao
import com.ultracommerce.core.search.dao.SolrIndexDao
import com.ultracommerce.core.search.domain.Field
import com.ultracommerce.core.search.domain.FieldEntity
import com.ultracommerce.core.search.service.solr.SolrHelperService
import com.ultracommerce.core.search.service.solr.SolrHelperServiceImpl
import com.ultracommerce.core.search.service.solr.SolrSearchServiceExtensionHandler
import com.ultracommerce.core.search.service.solr.index.SolrIndexServiceExtensionHandler
import com.ultracommerce.core.search.service.solr.index.SolrIndexServiceExtensionManager
import com.ultracommerce.core.search.service.solr.index.SolrIndexServiceImpl
import org.springframework.transaction.PlatformTransactionManager
import spock.lang.Specification

class SolrIndexServiceSpec extends Specification {
    
    SolrIndexServiceImpl service
    SolrIndexDao mockSolrIndexDao = Mock()
    IndexFieldDao mockFieldDao = Mock()
    PlatformTransactionManager mockTransactionManager = Mock()
    ProductDao mockProductDao = Mock()
    SkuDao mockSkuDao = Mock()
    LocaleService mockLocaleService = Mock()
    SolrClient mockSolrClient = Mock()
    SolrHelperService mockShs = Spy(SolrHelperServiceImpl)
    SolrIndexServiceExtensionManager mockExtensionManager = Mock()
    SandBoxHelper mockSandBoxHelper = Mock()
    
    def setup() {
        mockLocaleService.findAllLocales() >> new ArrayList<Locale>()
        mockExtensionManager.getProxy() >> Mock(SolrIndexServiceExtensionHandler)

        service = Spy(SolrIndexServiceImpl)
        service.solrIndexDao = mockSolrIndexDao
        service.indexFieldDao = mockFieldDao
        service.transactionManager = mockTransactionManager
        service.productDao = mockProductDao
        service.localeService = mockLocaleService
        service.shs = mockShs
        service.extensionManager = mockExtensionManager
        service.sandBoxHelper = mockSandBoxHelper
    }
    
}
