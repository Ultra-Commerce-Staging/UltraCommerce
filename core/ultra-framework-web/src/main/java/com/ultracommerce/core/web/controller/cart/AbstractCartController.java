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
package com.ultracommerce.core.web.controller.cart;

import com.ultracommerce.common.web.controller.UltraAbstractController;
import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.core.offer.service.OfferService;
import com.ultracommerce.core.order.service.OrderItemService;
import com.ultracommerce.core.order.service.OrderService;
import com.ultracommerce.core.payment.service.OrderToPaymentRequestDTOService;
import com.ultracommerce.core.web.service.UpdateCartService;

import javax.annotation.Resource;

/**
 * An abstract controller that provides convenience methods and resource declarations for its
 * children. Operations that are shared between controllers that deal with the catalog belong here.
 * 
 * @author apazzolini
 */
public abstract class AbstractCartController extends UltraAbstractController {
    
    @Resource(name = "ucCatalogService")
    protected CatalogService catalogService;
    
    @Resource(name = "ucOrderService")
    protected OrderService orderService;

    @Resource(name = "ucOrderItemService")
    protected OrderItemService orderItemService;
    
    @Resource(name = "ucOfferService")
    protected OfferService offerService;

    @Resource(name="ucUpdateCartService")
    protected UpdateCartService updateCartService;

    @Resource(name = "ucOrderToPaymentRequestDTOService")
    protected OrderToPaymentRequestDTOService dtoTranslationService;

}
