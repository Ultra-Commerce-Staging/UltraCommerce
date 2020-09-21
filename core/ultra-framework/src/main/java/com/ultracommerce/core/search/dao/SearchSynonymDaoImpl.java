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
package com.ultracommerce.core.search.dao;

import com.ultracommerce.core.search.domain.SearchSynonym;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository("ucSearchSynonymDao")
public class SearchSynonymDaoImpl implements SearchSynonymDao {

    @PersistenceContext(unitName = "ucPU")
    protected EntityManager em;

    @SuppressWarnings("unchecked")
    public List<SearchSynonym> getAllSynonyms() {
        Query query = em.createNamedQuery("UC_READ_SEARCH_SYNONYMS");
        List<SearchSynonym> result;
        try {
            result = (List<SearchSynonym>) query.getResultList();
        } catch (NoResultException e) {
            result = null;
        }
        return result;
    }

    public void createSynonym(SearchSynonym synonym) {
        em.persist(synonym);
    }

    public void deleteSynonym(SearchSynonym synonym) {
        em.remove(synonym);
    }

    public void updateSynonym(SearchSynonym synonym) {
        em.merge(synonym);
    }
}
