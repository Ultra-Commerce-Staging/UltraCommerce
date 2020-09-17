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
package com.ultracommerce.admin.web.controller.extension;

import com.ultracommerce.admin.web.controller.entity.AdminOfferController;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.presentation.client.RuleBuilderDisplayType;
import com.ultracommerce.core.offer.domain.OfferAdminPresentation;
import com.ultracommerce.openadmin.web.controller.AbstractAdminAbstractControllerExtensionHandler;
import com.ultracommerce.openadmin.web.controller.AdminAbstractControllerExtensionManager;
import com.ultracommerce.openadmin.web.form.entity.EntityForm;
import com.ultracommerce.openadmin.web.form.entity.Field;
import com.ultracommerce.openadmin.web.form.entity.FieldGroup;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Component("ucAdminOfferControllerExtensionHandler")
public class AdminOfferControllerExtensionHandler extends AbstractAdminAbstractControllerExtensionHandler {

    @Resource(name = "ucAdminAbstractControllerExtensionManager")
    protected AdminAbstractControllerExtensionManager extensionManager;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    @Override
    public ExtensionResultStatusType setAdditionalModelAttributes(Model model, String sectionKey) {
        if (AdminOfferController.SECTION_KEY.equals(sectionKey)) {
            EntityForm form = (EntityForm) model.asMap().get("entityForm");

            if (form != null) {
                //UX Meta-Data to display the Rule Builders on the Offer Screen
                FieldGroup ruleConfigGroup = form.findGroup(OfferAdminPresentation.GroupName.RuleConfiguration);
                if (ruleConfigGroup != null) {
                    ruleConfigGroup.getGroupAttributes().put("additionalGroupClasses", "card");
                }

                // Qualifier and Target Item Builders
                Field qualField = form.findField("qualifyingItemCriteria");
                if (qualField != null) {
                    qualField.setDisplayType(RuleBuilderDisplayType.NORMAL.name());
                }
                Field fgField = form.findField("offerMatchRules---FULFILLMENT_GROUP");
                if (fgField != null) {
                    fgField.setDisplayType(RuleBuilderDisplayType.NORMAL.name());
                }
                Field tarField = form.findField("targetItemCriteria");
                if (tarField != null) {
                    tarField.setDisplayType(RuleBuilderDisplayType.NORMAL.name());
                }

                //Activity Range Builder
                Field timeField = form.findField("offerMatchRules---TIME");
                if (timeField != null) {
                    timeField.setDisplayType(RuleBuilderDisplayType.MODAL.name());
                }

                //Usage Builders
                Field custField = form.findField("offerMatchRules---CUSTOMER");
                if (custField != null) {
                    custField.setDisplayType(RuleBuilderDisplayType.MODAL.name());
                }

                Field orderField = form.findField("offerMatchRules---ORDER");
                if (orderField != null) {
                    orderField.setDisplayType(RuleBuilderDisplayType.MODAL.name());
                }
            }
        }

        return ExtensionResultStatusType.HANDLED_CONTINUE;
    }
}
