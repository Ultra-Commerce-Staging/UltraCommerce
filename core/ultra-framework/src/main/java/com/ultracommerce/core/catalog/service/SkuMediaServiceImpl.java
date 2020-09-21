/*
 * #%L
 * UltraCommerce Framework
 * %%
 * Copyright (C) 2009 - 2019 Ultra Commerce
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
package com.ultracommerce.core.catalog.service;

import com.ultracommerce.core.catalog.dao.SkuMediaDao;
import com.ultracommerce.core.catalog.domain.OrderedSkuMediaXref;
import com.ultracommerce.core.catalog.domain.SkuMediaXref;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

@Service("ucSkuMediaService")
public class SkuMediaServiceImpl implements SkuMediaService {

    @Resource(name="ucSkuMediaDao")
    protected SkuMediaDao skuMediaDao;

    @Autowired
    protected Environment env;

    @Override
    @Transactional
    public SkuMediaXref save(SkuMediaXref skuMediaXref) {
        return skuMediaDao.save(skuMediaXref);
    }

    @Override
    public List<SkuMediaXref> findSkuMediaBySkuId(Long skuId) {
        List<SkuMediaXref> skuMediaXrefs = skuMediaDao.readSkuMediaBySkuId(skuId);

        if (isOrderedSkuMediaEnabled()) {
            skuMediaXrefs = sort(skuMediaXrefs);
        }

        return skuMediaXrefs;
    }

    protected List<SkuMediaXref> sort(List<SkuMediaXref> skuMediaXrefs) {
        skuMediaXrefs.sort(Comparator.comparing(xref -> ((OrderedSkuMediaXref) xref).getDisplayOrder()));

        return skuMediaXrefs;
    }

    protected boolean isOrderedSkuMediaEnabled() {
        return env.getProperty("sku.media.display-order.enabled", boolean.class, false);
    }

}
