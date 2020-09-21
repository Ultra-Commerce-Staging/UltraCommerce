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

import com.ultracommerce.core.search.redirect.domain.SearchRedirectImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * @deprecated Replaced in functionality by {@link SearchRedirectImpl}
 */
@Deprecated
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucStandardElements")
public class SearchInterceptImpl implements SearchIntercept {
    
    @Id
    @GeneratedValue(generator = "SearchInterceptId")
    @GenericGenerator(
        name="SearchInterceptId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="SearchInterceptImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.core.search.domain.SearchInterceptImpl")
        }
    )
    @Column(name = "SEARCH_INTERCEPT_ID")
    protected Long id;
    
    @Column(name = "TERM")
    @Index(name="SEARCHINTERCEPT_TERM_INDEX", columnNames={"TERM"})
    private String term;
    
    @Column(name = "REDIRECT")
    private String redirect;

    /* (non-Javadoc)
     * @see com.ultracommerce.core.search.domain.SearchIntercept#getTerm()
     */
    @Override
    public String getTerm() {
        return term;
    }
    /* (non-Javadoc)
     * @see com.ultracommerce.core.search.domain.SearchIntercept#setTerm(java.lang.String)
     */
    @Override
    public void setTerm(String term) {
        this.term = term;
    }
    /* (non-Javadoc)
     * @see com.ultracommerce.core.search.domain.SearchIntercept#getRedirect()
     */
    @Override
    public String getRedirect() {
        return redirect;
    }
    /* (non-Javadoc)
     * @see com.ultracommerce.core.search.domain.SearchIntercept#setRedirect(java.lang.String)
     */
    @Override
    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
