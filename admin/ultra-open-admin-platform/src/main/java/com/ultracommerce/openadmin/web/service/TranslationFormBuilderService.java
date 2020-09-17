/*
 * #%L
 * UltraCommerce Open Admin Platform
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

package com.ultracommerce.openadmin.web.service;

import com.ultracommerce.common.i18n.domain.Translation;
import com.ultracommerce.openadmin.dto.ClassMetadata;
import com.ultracommerce.openadmin.web.form.TranslationForm;
import com.ultracommerce.openadmin.web.form.component.ListGrid;
import com.ultracommerce.openadmin.web.form.entity.EntityForm;

import java.util.List;

public interface TranslationFormBuilderService {

    /**
     * Builds a ListGrid for the given list of translations
     * 
     * @param translations
     * @param isRte - whether or not the field that this translation is tied to is a rich text edit field
     * @return the list grid
     */
    public ListGrid buildListGrid(List<Translation> translations, boolean isRte);

    /**
     * Builds an EntityForm used to create or edit a translation value
     * 
     *
     * @param cmd
     * @param formProperties
     * @param action (values "add", "update" or "other")
     * @return the entity form
     */
    public EntityForm buildTranslationForm(ClassMetadata cmd, TranslationForm formProperties, TranslationFormAction action);

}
