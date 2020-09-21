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

/**
 * The SeoDefaultPropertyService is responsible for providing default values for a Product, Category, or Page's SEO properties.
 * 
 * @author Chris Kittrell (ckittrell)
 */
public interface SeoDefaultPropertyService {

    String getProductTitlePattern(Product product);

    String getCategoryTitlePattern();

    String getTitle(PageDTO page);

    String getType(Product product);

    String getType(Category category);

    String getType(PageDTO page);

    String getProductDescriptionPattern(Product product);

    String getCategoryDescriptionPattern();

    String getDescription(PageDTO page);

    String getUrl(Product product);

    String getUrl(Category category);

    String getUrl(PageDTO page);

    String getImage(Product product);

    String getImage(Category category);

    String getImage(PageDTO page);

    String getCanonicalUrl(Product product);

    String getPaginationPrevUrl(Product product);

    String getPaginationNextUrl(Product product);

    String getCanonicalUrl(Category category);

    String getPaginationPrevUrl(Category category);

    String getPaginationNextUrl(Category category);
}
