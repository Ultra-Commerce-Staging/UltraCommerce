/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.pricing.service.workflow;

import org.apache.commons.lang.StringUtils;
import com.ultracommerce.common.rule.MvelHelper;
import com.ultracommerce.common.util.EfficientLRUMap;
import com.ultracommerce.core.catalog.domain.SkuFee;
import com.ultracommerce.core.catalog.service.type.SkuFeeType;
import com.ultracommerce.core.order.domain.BundleOrderItem;
import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentGroupFee;
import com.ultracommerce.core.order.domain.FulfillmentGroupItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.service.FulfillmentGroupService;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 
 * @author Phillip Verheyden
 */
@Component("ucConsolidateFulfillmentFeesActivity")
public class ConsolidateFulfillmentFeesActivity extends BaseActivity<ProcessContext<Order>> {
    
    public static final int ORDER = 2000;
    
    @SuppressWarnings("unchecked")
    protected static final Map EXPRESSION_CACHE = new EfficientLRUMap(1000);
    
    @Resource(name = "ucFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;
    
    public ConsolidateFulfillmentFeesActivity() {
        setOrder(ORDER);
    }

    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        Order order = context.getSeedData();
        
        for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            //create and associate all the Fulfillment Fees
            for (FulfillmentGroupItem item : fulfillmentGroup.getFulfillmentGroupItems()) {
                List<SkuFee> fees = null;
                if (item.getOrderItem() instanceof BundleOrderItem) {
                    fees = ((BundleOrderItem)item.getOrderItem()).getSku().getFees();
                } else if (item.getOrderItem() instanceof DiscreteOrderItem) {
                    fees = ((DiscreteOrderItem)item.getOrderItem()).getSku().getFees();
                }
                
                if (fees != null) {
                    for (SkuFee fee : fees) {
                        if (SkuFeeType.FULFILLMENT.equals(fee.getFeeType())) {
                            if (shouldApplyFeeToFulfillmentGroup(fee, fulfillmentGroup)) {
                                FulfillmentGroupFee fulfillmentFee = fulfillmentGroupService.createFulfillmentGroupFee();
                                fulfillmentFee.setName(fee.getName());
                                fulfillmentFee.setTaxable(fee.getTaxable());
                                fulfillmentFee.setAmount(fee.getAmount());
                                fulfillmentFee.setFulfillmentGroup(fulfillmentGroup);
                                
                                fulfillmentGroup.addFulfillmentGroupFee(fulfillmentFee);
                            }
                        }
                    }
                }
            }
            
            if (fulfillmentGroup.getFulfillmentGroupFees().size() > 0) {
                fulfillmentGroup = fulfillmentGroupService.save(fulfillmentGroup);
            }
        }
        
        context.setSeedData(order);
        return context;
    }

    /**
     * If the SkuFee expression is null or empty, this method will always return true
     * 
     * @param fee
     * @param fulfillmentGroup
     * @return
     */
    protected boolean shouldApplyFeeToFulfillmentGroup(SkuFee fee, FulfillmentGroup fulfillmentGroup) {
        boolean appliesToFulfillmentGroup = true;
        String feeExpression = fee.getExpression();
        
        if (StringUtils.isNotEmpty(feeExpression)) {
            synchronized (EXPRESSION_CACHE) {
                HashMap<String, Object> vars = new HashMap<>();
                vars.put("fulfillmentGroup", fulfillmentGroup);
                MvelHelper.evaluateRule(feeExpression, vars, EXPRESSION_CACHE);
            }
        }
        
        return appliesToFulfillmentGroup;
    }

}
