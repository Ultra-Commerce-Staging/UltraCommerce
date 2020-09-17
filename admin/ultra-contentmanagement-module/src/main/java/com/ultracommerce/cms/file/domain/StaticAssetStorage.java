/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.file.domain;

import java.sql.Blob;

/**
 * Created by IntelliJ IDEA.
 * User: jfischer
 * Date: 9/8/11
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StaticAssetStorage {

    Long getId();

    void setId(Long id);

    Blob getFileData();

    void setFileData(Blob fileData);

    public Long getStaticAssetId();

    public void setStaticAssetId(Long staticAssetId);

}
