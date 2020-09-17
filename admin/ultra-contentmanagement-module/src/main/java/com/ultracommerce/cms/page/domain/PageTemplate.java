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
package com.ultracommerce.cms.page.domain;

import com.ultracommerce.cms.field.domain.FieldGroup;
import com.ultracommerce.common.copy.MultiTenantCloneable;
import com.ultracommerce.common.locale.domain.Locale;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bpolster.
 */
public interface PageTemplate extends Serializable, MultiTenantCloneable<PageTemplate> {

    public Long getId();

    public void setId(Long id);

    public String getTemplateName();

    public void setTemplateName(String templateName);

    public String getTemplateDescription();

    public void setTemplateDescription(String templateDescription);

    public String getTemplatePath();

    public void setTemplatePath(String templatePath);

    /**
     * @deprecated in favor of translating individual fields
     * @return
     */
    public Locale getLocale();

    /**
     * @deprecated in favor of translating individual fields
     * @return
     */
    public void setLocale(Locale locale);

    @Deprecated
    public List<FieldGroup> getFieldGroups();

    @Deprecated
    public void setFieldGroups(List<FieldGroup> fieldGroups);

    public List<PageTemplateFieldGroupXref> getFieldGroupXrefs();

    public void setFieldGroupXrefs(List<PageTemplateFieldGroupXref> fieldGroups);

}
