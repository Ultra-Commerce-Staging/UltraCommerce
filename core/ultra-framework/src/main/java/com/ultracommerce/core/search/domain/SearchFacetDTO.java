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
package com.ultracommerce.core.search.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andre Azzolini (apazzolini)
 */
public class SearchFacetDTO {
    
    protected SearchFacet facet;
    protected boolean showQuantity;
    protected List<SearchFacetResultDTO> facetValues = new ArrayList<SearchFacetResultDTO>();
    protected boolean active;
    protected String abbreviation;
    
    public SearchFacet getFacet() {
        return facet;
    }
    
    public void setFacet(SearchFacet facet) {
        this.facet = facet;
    }
    
    public boolean isShowQuantity() {
        return showQuantity;
    }
    
    public void setShowQuantity(boolean showQuantity) {
        this.showQuantity = showQuantity;
    }
    
    public List<SearchFacetResultDTO> getFacetValues() {
        return facetValues;
    }
    
    public void setFacetValues(List<SearchFacetResultDTO> facetValues) {
        this.facetValues = facetValues;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public String getAbbreviation() {
        if (abbreviation != null) {
            return abbreviation;
        }
        
        return this.getFacet().getField().getAbbreviation();
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
    
}
