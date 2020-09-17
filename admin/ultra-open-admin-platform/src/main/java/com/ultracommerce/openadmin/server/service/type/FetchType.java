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
package com.ultracommerce.openadmin.server.service.type;

import com.ultracommerce.openadmin.web.form.component.ListGrid;

/**
 * Defines the type of fetch and paging technique to be used: {@link #LARGERESULTSET} denotes a lastid approach, rather than an offset, while
 * {@link #DEFAULT} denotes a standard offset and page size technique.
 * </p>
 * This is used primarily to inform the type of UI paging component. See {@link ListGrid#fetchType}.
 *
 * @author Jeff Fischer
 */
public enum FetchType {
    LARGERESULTSET,DEFAULT
}
