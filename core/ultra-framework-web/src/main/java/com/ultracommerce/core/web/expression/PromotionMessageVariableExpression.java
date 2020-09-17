/*
 * #%L
 * UltraCommerce Framework Web
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
package com.ultracommerce.core.web.expression;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.core.catalog.domain.Product;
import com.ultracommerce.core.offer.domain.OrderItemPriceDetailAdjustment;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.domain.OrderItemPriceDetail;
import com.ultracommerce.core.promotionMessage.domain.type.PromotionMessagePlacementType;
import com.ultracommerce.core.promotionMessage.dto.PromotionMessageDTO;
import com.ultracommerce.core.promotionMessage.service.PromotionMessageGenerator;
import com.ultracommerce.core.promotionMessage.util.UCPromotionMessageUtils;
import com.ultracommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @author Chris Kittrell (ckittrell)
 */
@Service("ucPromotionMessageVariableExpression")
@ConditionalOnTemplating
public class PromotionMessageVariableExpression extends UCVariableExpression {

    private static final Log LOG = LogFactory.getLog(PromotionMessageVariableExpression.class);

    public static final String PRODUCT = "product";
    public static final String PLACEMENT = "placement";

    @Resource(name = "ucPromotionMessageGenerators")
    protected List<PromotionMessageGenerator> generators;

    @Override
    public String getName() {
        return "promotion_messages";
    }
    
    public List<PromotionMessageDTO> getProductPromotionMessages(Product product, String... placements) {
        List<String> filteredPlacements = filterInvalidPlacements(placements);
        if (!filteredPlacements.contains(PromotionMessagePlacementType.EVERYWHERE.getType())) {
            filteredPlacements.add(PromotionMessagePlacementType.EVERYWHERE.getType());
        }
        
        Map<String, List<PromotionMessageDTO>> promotionMessages = new MultiValueMap();
        for (PromotionMessageGenerator generator : generators) {
            promotionMessages.putAll(generator.generatePromotionMessages(product));
        }

        List<PromotionMessageDTO> filteredMessages = UCPromotionMessageUtils.filterPromotionMessageDTOsByTypes(promotionMessages, filteredPlacements);
        UCPromotionMessageUtils.sortMessagesByPriority(filteredMessages);

        return filteredMessages;
    }
    
    public List<String> getItemPromotionMessages(OrderItem orderItem) {
        List<String> appliedOfferNames = getAppliedOfferNamesForOrderItem(orderItem);
        for (OrderItem child : orderItem.getChildOrderItems()) {
            appliedOfferNames.addAll(getAppliedOfferNamesForOrderItem(child));
        }
        return appliedOfferNames;
    }
    
    protected List<String> getAppliedOfferNamesForOrderItem(OrderItem orderItem) {
        List<String> appliedOfferNames = new ArrayList<>();
        for (OrderItemPriceDetail oipd : orderItem.getOrderItemPriceDetails()) {
            for (OrderItemPriceDetailAdjustment adjustment : oipd.getOrderItemPriceDetailAdjustments()) {
                appliedOfferNames.add(adjustment.getOfferName());
            }
        }
        return appliedOfferNames;
    }
    
    protected List<String> filterInvalidPlacements(String[] placements) {
        List<String> requestedPlacement = new ArrayList<>();
        for (String placement : placements) {
            placement = placement.trim();
            if (isValidPlacementType(placement)) {
                requestedPlacement.add(placement);
            } else {
                LOG.warn("Stripping out invalid promotion message placement " + placement + ". See PromotionMessagePlacementType for valid placements");
            }
        }
        return requestedPlacement;
    }

    protected boolean isValidPlacementType(String placement) {
        return PromotionMessagePlacementType.getInstance(placement) != null;
    }

}
