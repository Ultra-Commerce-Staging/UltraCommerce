/*
 * #%L
 * UltraCommerce Common Libraries
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
package com.ultracommerce.core.promotionMessage.util;

import org.apache.commons.lang3.ObjectUtils;
import com.ultracommerce.common.util.UCCollectionUtils;
import com.ultracommerce.common.util.TypedTransformer;
import com.ultracommerce.core.promotionMessage.domain.PromotionMessage;
import com.ultracommerce.core.promotionMessage.domain.type.PromotionMessagePlacementType;
import com.ultracommerce.core.promotionMessage.dto.PromotionMessageDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Convenience methods for interacting with {@link PromotionMessage}s
 * 
 * @author Chris Kittrell (ckittrell)
 */
public class UCPromotionMessageUtils {
    
    /**
     * Given a list of {@link PromotionMessage}s, gather their message properties.
     * 
     * @param promotionMessages
     * @return messages
     */
    public static List<String> gatherMessages(List<PromotionMessage> promotionMessages) {
        return UCCollectionUtils.collectList(promotionMessages, new TypedTransformer<String>() {
            @Override
            public String transform(Object input) {
                PromotionMessage promotionMessage = (PromotionMessage) input;
                return promotionMessage.getMessage();
            }
        });
    }

    /**
     * Given a list of {@link PromotionMessageDTO}s, gather their message properties.
     *
     * @param promotionMessages
     * @return messages
     */
    public static List<String> gatherMessagesFromDTOs(List<PromotionMessageDTO> promotionMessages) {
        return UCCollectionUtils.collectList(promotionMessages, new TypedTransformer<String>() {
            @Override
            public String transform(Object input) {
                PromotionMessageDTO promotionMessage = (PromotionMessageDTO) input;
                return promotionMessage.getMessage();
            }
        });
    }

    /**
     * Given a map of {@link PromotionMessagePlacementType}s to {@link PromotionMessageDTO}s, sort based on priority
     *
     * @param promotionMessages
     */
    public static void sortMessagesByPriority(Map<String, List<PromotionMessageDTO>> promotionMessages) {
        for (String key : promotionMessages.keySet()) {
            List<PromotionMessageDTO> messages = promotionMessages.get(key);
            sortMessagesByPriority(messages);
        }
    }

    /**
     * Given a list of {@link PromotionMessageDTO}s, sort based on priority
     *
     * @param promotionMessages
     */
    public static void sortMessagesByPriority(List<PromotionMessageDTO> promotionMessages) {
        Collections.sort(promotionMessages, new Comparator<PromotionMessageDTO>() {
            @Override
            public int compare(PromotionMessageDTO o1, PromotionMessageDTO o2) {
                return ObjectUtils.compare(o1.getPriority(), o2.getPriority(), true);
            }
        });
    }

    /**
     * Given a map of {@link PromotionMessagePlacementType}s to {@link PromotionMessageDTO}s, gather the message properties.
     *
     * @param promotionMessageMap
     * @param type
     * @return messages
     */
    public static List<String> gatherMessagesByPlacementType(Map<String, List<PromotionMessageDTO>> promotionMessageMap,
            PromotionMessagePlacementType type) {
        List<PromotionMessageDTO> promotionMessageDTOs = promotionMessageMap.get(type.getType());

        if (promotionMessageDTOs == null) {
            return new ArrayList<>();
        }

        return UCCollectionUtils.collectList(promotionMessageDTOs, new TypedTransformer<String>() {
            @Override
            public String transform(Object input) {
                PromotionMessageDTO promotionMessageDTO = (PromotionMessageDTO) input;
                return promotionMessageDTO.getMessage();
            }
        });
    }

    /**
     * Given a map of {@link PromotionMessagePlacementType}s to {@link PromotionMessageDTO}s and a list
     * of {@link PromotionMessagePlacementType}s, filter the map into a list.
     *
     * @param promotionMessages
     * @param placementTypes
     * @return a list of filtered message DTOs
     */
    public static List<PromotionMessageDTO> filterPromotionMessageDTOsByTypes(Map<String, List<PromotionMessageDTO>> promotionMessages,
            List<String> placementTypes) {
        List<PromotionMessageDTO> filteredPromotionMessages = new ArrayList<>();

        for (String type : promotionMessages.keySet()) {
            if (placementTypes.contains(type)) {
                filteredPromotionMessages.addAll(promotionMessages.get(type));
            }
        }
        return filteredPromotionMessages;
    }
}
