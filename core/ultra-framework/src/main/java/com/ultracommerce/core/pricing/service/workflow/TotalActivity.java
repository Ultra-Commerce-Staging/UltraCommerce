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

import com.ultracommerce.common.currency.util.UltraCurrencyUtils;
import com.ultracommerce.common.money.Money;
import com.ultracommerce.core.order.domain.FulfillmentGroup;
import com.ultracommerce.core.order.domain.FulfillmentGroupFee;
import com.ultracommerce.core.order.domain.FulfillmentGroupItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.TaxDetail;
import com.ultracommerce.core.workflow.BaseActivity;
import com.ultracommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * The TotalActivity is responsible for calculating and setting totals for a given order.
 * It must set the sum of the the taxes in the appropriate places as well as fulfillment
 * group subtotals / totals and order subtotals / totals.
 * 
 * @author aazzolini
 *
 */
@Component("ucTotalActivity")
public class TotalActivity extends BaseActivity<ProcessContext<Order>> {

    public static final int ORDER = 8000;
    
    public TotalActivity() {
        setOrder(ORDER);
    }
    
    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        Order order = context.getSeedData();
        
        setTaxSums(order);
        
        Money total = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, order.getCurrency());
        total = total.add(order.getSubTotal());
        total = total.subtract(order.getOrderAdjustmentsValue());
        total = total.add(order.getTotalShipping());
        // There may not be any taxes on the order
        if (order.getTotalTax() != null) {
            total = total.add(order.getTotalTax());
        }

        Money fees = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, order.getCurrency());
        for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            Money fgTotal = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, order.getCurrency());
            fgTotal = fgTotal.add(fulfillmentGroup.getMerchandiseTotal());
            fgTotal = fgTotal.add(fulfillmentGroup.getShippingPrice());
            fgTotal = fgTotal.add(fulfillmentGroup.getTotalTax());
            
            for (FulfillmentGroupFee fulfillmentGroupFee : fulfillmentGroup.getFulfillmentGroupFees()) {
                fgTotal = fgTotal.add(fulfillmentGroupFee.getAmount());
                fees = fees.add(fulfillmentGroupFee.getAmount());
            }
            
            fulfillmentGroup.setTotal(fgTotal);
        }

        total = total.add(fees);
        order.setTotal(total);
        
        context.setSeedData(order);
        return context;
    }
    
    protected void setTaxSums(Order order) {
        if (order.getTaxOverride()) {
            Money zeroMoney = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, order.getCurrency());

            for (FulfillmentGroup fg : order.getFulfillmentGroups()) {
                if (fg.getTaxes() != null) {
                    fg.getTaxes().clear();
                }
                fg.setTotalTax(zeroMoney);
                
                for (FulfillmentGroupItem fgi : fg.getFulfillmentGroupItems()) {
                    if (fgi.getTaxes() != null) {
                        fgi.getTaxes().clear();
                    }
                    fgi.setTotalTax(zeroMoney);
                }
                
                for (FulfillmentGroupFee fee : fg.getFulfillmentGroupFees()) {
                    if (fee.getTaxes() != null) {
                        fee.getTaxes().clear();
                    }
                    fee.setTotalTax(zeroMoney);
                }

                fg.setTotalFulfillmentGroupTax(zeroMoney);
                fg.setTotalItemTax(zeroMoney);
                fg.setTotalFeeTax(zeroMoney);
            }

            order.setTotalTax(zeroMoney);

            return;
        }

        Money orderTotalTax = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, order.getCurrency());
        
        for (FulfillmentGroup fg : order.getFulfillmentGroups()) {
            Money fgTotalFgTax = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, order.getCurrency());
            Money fgTotalItemTax = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, order.getCurrency());
            Money fgTotalFeeTax = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, order.getCurrency());
            
            // Add in all FG specific taxes (such as shipping tax)
            if (fg.getTaxes() != null) {
                for (TaxDetail tax : fg.getTaxes()) {
                    fgTotalFgTax = fgTotalFgTax.add(tax.getAmount());
                }
            }
            
            for (FulfillmentGroupItem item : fg.getFulfillmentGroupItems()) {
                Money itemTotalTax = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, order.getCurrency());
                
                // Add in all taxes for this item
                if (item.getTaxes() != null) {
                    for (TaxDetail tax : item.getTaxes()) {
                        itemTotalTax = itemTotalTax.add(tax.getAmount());
                    }
                }
                
                item.setTotalTax(itemTotalTax);
                fgTotalItemTax = fgTotalItemTax.add(itemTotalTax);
            }
            
            for (FulfillmentGroupFee fee : fg.getFulfillmentGroupFees()) {
                Money feeTotalTax = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, order.getCurrency());
                
                // Add in all taxes for this fee
                if (fee.getTaxes() != null) {
                    for (TaxDetail tax : fee.getTaxes()) {
                        feeTotalTax = feeTotalTax.add(tax.getAmount());
                    }
                }
                
                fee.setTotalTax(feeTotalTax);
                fgTotalFeeTax = fgTotalFeeTax.add(feeTotalTax);
            }
            
            Money fgTotalTax = UltraCurrencyUtils.getMoney(BigDecimal.ZERO, order.getCurrency()).add(fgTotalFgTax).add(fgTotalItemTax).add(fgTotalFeeTax);
            
            // Set the fulfillment group tax sums
            fg.setTotalFulfillmentGroupTax(fgTotalFgTax);
            fg.setTotalItemTax(fgTotalItemTax);
            fg.setTotalFeeTax(fgTotalFeeTax);
            fg.setTotalTax(fgTotalTax);
            
            orderTotalTax = orderTotalTax.add(fgTotalTax);
        }
        
        order.setTotalTax(orderTotalTax);
    }
}
