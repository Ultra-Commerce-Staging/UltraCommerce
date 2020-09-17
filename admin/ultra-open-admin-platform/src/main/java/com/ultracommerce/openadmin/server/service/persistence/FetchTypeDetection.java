/*
 * #%L
 * ultra-enterprise
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
package com.ultracommerce.openadmin.server.service.persistence;

import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.server.service.type.FetchType;

/**
 * Detect the type of fetch and paging being used for a particular admin pipeline fetch request
 * </p>
 * Also, {@link #shouldPromptForSearch(PersistencePackage, CriteriaTransferObject)} designates whether this fetch
 * request should be considered empty and instead prompt the user to enter a search term in the listgrid before
 * retrieving records. This save a wasted default retrieval delay, which could be impactful.
 *
 * @author Jeff Fischer
 */
public interface FetchTypeDetection {

    /**
     * Detect whether or not this list grid will fetch records using infinite scroll and traditional paging
     * ({@link FetchType#DEFAULT}), or next and previous page links with scrolling only for the current page of records
     * ({@link FetchType#LARGERESULTSET}).
     *
     * @param persistencePackage
     * @param cto
     * @return
     */
    FetchType getFetchType(PersistencePackage persistencePackage, CriteriaTransferObject cto);

    /**
     * Designates whether this fetch request should be considered empty and instead prompt the user to enter a search term
     * in the listgrid before retrieving records. This save a wasted default retrieval delay, which could be impactful.
     * </p>
     * This should not include filtered list grids, as a filtered list grid indicates a search has been
     * performed, in which case, you always want to return the records found.
     *
     * @param persistencePackage
     * @param cto
     * @return
     */
    boolean shouldPromptForSearch(PersistencePackage persistencePackage, CriteriaTransferObject cto);

}
