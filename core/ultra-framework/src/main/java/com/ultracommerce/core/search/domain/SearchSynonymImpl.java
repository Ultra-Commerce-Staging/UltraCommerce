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

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UC_SEARCH_SYNONYM")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="ucStandardElements")
public class SearchSynonymImpl implements SearchSynonym {
    
    @Id
    @GeneratedValue(generator = "SearchSynonymId")
    @GenericGenerator(
        name="SearchSynonymId",
        strategy="com.ultracommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="SearchSynonymImpl"),
            @Parameter(name="entity_name", value="com.ultracommerce.core.search.domain.SearchSynonymImpl")
        }
    )
    @Column(name = "SEARCH_SYNONYM_ID")
    private Long id;
    
    @Column(name = "TERM")
    @Index(name="SEARCHSYNONYM_TERM_INDEX", columnNames={"TERM"})
    private String term;
    
    @Column(name = "SYNONYMS")
    private String synonyms;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Override
    public String getTerm() {
        return term;
    }
    @Override
    public void setTerm(String term) {
        this.term = term;
    }
    @Override
    public String[] getSynonyms() {
        return synonyms.split("\\|");
    }
    @Override
    public void setSynonyms(String[] synonyms) {
        this.synonyms = StringUtils.join(synonyms, '|');
    }
    
}
