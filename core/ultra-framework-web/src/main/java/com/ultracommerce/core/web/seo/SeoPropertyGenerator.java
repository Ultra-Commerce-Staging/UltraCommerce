/*
 * #%L
 * ultra-enterprise
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
package com.ultracommerce.core.web.seo;

import com.ultracommerce.common.page.dto.PageDTO;
import com.ultracommerce.core.catalog.domain.Category;
import com.ultracommerce.core.catalog.domain.Product;

import java.util.Map;

/**
 * A {@link SeoPropertyGenerator} understands how to gather a specific set of SEO properties for the given domain object
 * 
 * @author Chris Kittrell (ckittrell)
 */
public interface SeoPropertyGenerator {
    
    Map<String, String> filterForSeoProperties(Map<String, String> properties);

    Map<String, String> gatherSeoProperties(Category category);

    Map<String, String> gatherSeoProperties(Product product);

    Map<String, String> gatherSeoProperties(PageDTO page);

}
