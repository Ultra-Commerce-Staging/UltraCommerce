/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.search.service.solr;

import org.apache.commons.collections4.ListUtils;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Nick Crum ncrum
 */
@Service("ucSolrJSONFacetService")
public class SolrJSONFacetServiceImpl implements SolrJSONFacetService {

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, SolrJSONFacet> resolveJSONFacetResponse(QueryResponse response) {
        Map<String, SolrJSONFacet> jsonFacetMap = new HashMap<>();
        NamedList facetResponse = (NamedList) response.getResponse().get("facets");
        if (facetResponse != null) {
            Iterator<Map.Entry<String, Object>> facetIterator = facetResponse.iterator();
            while (facetIterator.hasNext()) {
                Map.Entry<String, Object> entry = facetIterator.next();

                if (NamedList.class.isAssignableFrom(entry.getValue().getClass())) {
                    jsonFacetMap.put(entry.getKey(), resolveJSONFacet((NamedList) entry.getValue()));
                }
            }
        }

        return jsonFacetMap;
    }

    @SuppressWarnings("unchecked")
    protected SolrJSONFacet resolveJSONFacet(NamedList facetNamedList) {
        SolrJSONFacet jsonFacet = new SolrJSONFacet();
        Iterator<Map.Entry<String, Object>> iterator = facetNamedList.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();

            if (entry.getValue() != null) {
                if (NamedList.class.isAssignableFrom(entry.getValue().getClass())) {
                    jsonFacet.getMap().put(entry.getKey(), resolveJSONFacet((NamedList) entry.getValue()));
                } else if (List.class.isAssignableFrom(entry.getValue().getClass())) {
                    jsonFacet.getMap().put(entry.getKey(), resolveJSONFacetList((List<NamedList>) entry.getValue()));
                } else {
                    jsonFacet.getMap().put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
        }

        return jsonFacet;
    }

    @SuppressWarnings("unchecked")
    protected List<SolrJSONFacet> resolveJSONFacetList(List<NamedList> listOfNamedList) {
        List<SolrJSONFacet> listOfJSONFacet = new ArrayList<>();
        for (NamedList namedList : ListUtils.emptyIfNull(listOfNamedList)) {
            listOfJSONFacet.add(resolveJSONFacet(namedList));
        }
        return listOfJSONFacet;
    }
}
