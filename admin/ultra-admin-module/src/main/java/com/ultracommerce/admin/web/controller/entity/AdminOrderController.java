/*
 * #%L
 * UltraCommerce Admin Module
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
package com.ultracommerce.admin.web.controller.entity;

import org.apache.commons.collections.CollectionUtils;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.openadmin.web.controller.entity.AdminBasicEntityController;
import com.ultracommerce.openadmin.web.form.component.ListGrid;
import com.ultracommerce.openadmin.web.form.entity.EntityForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Handles admin operations for the {@link Order} entity. 
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Controller("ucAdminOrderController")
@RequestMapping("/" + AdminOrderController.SECTION_KEY)
public class AdminOrderController extends AdminBasicEntityController {
    
    public static final String SECTION_KEY = "order";
    
    @Override
    protected String getSectionKey(Map<String, String> pathVars) {
        //allow external links to work for ToOne items
        if (super.getSectionKey(pathVars) != null) {
            return super.getSectionKey(pathVars);
        }
        return SECTION_KEY;
    }
    
    @Override
    protected String showViewUpdateCollection(HttpServletRequest request, Model model, Map<String, String> pathVars,
            String id, String collectionField, String collectionItemId, String modalHeaderType) throws ServiceException {
        String returnPath = super.showViewUpdateCollection(request, model, pathVars, id, collectionField, collectionItemId,
                modalHeaderType);
        
        if ("orderItems".equals(collectionField)) {
            EntityForm ef = (EntityForm) model.asMap().get("entityForm");

            ListGrid adjustmentsGrid = ef.findListGrid("orderItemAdjustments");
            if (adjustmentsGrid != null && CollectionUtils.isEmpty(adjustmentsGrid.getRecords())) {
                ef.removeListGrid("orderItemAdjustments");
            }

            ListGrid priceDetailsGrid = ef.findListGrid("orderItemPriceDetails");
            if (priceDetailsGrid != null && CollectionUtils.isEmpty(priceDetailsGrid.getRecords())) {
                ef.removeListGrid("orderItemPriceDetails");
            }
        }
        
        return returnPath;
    }

}
