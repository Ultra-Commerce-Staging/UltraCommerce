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
package com.ultracommerce.core.web.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.util.UCSystemProperty;
import com.ultracommerce.common.util.StringUtil;
import com.ultracommerce.common.web.UltraRequestContext;
import com.ultracommerce.core.catalog.domain.ProductOptionValue;
import com.ultracommerce.core.catalog.domain.Sku;
import com.ultracommerce.core.catalog.service.CatalogService;
import com.ultracommerce.core.order.domain.BundleOrderItem;
import com.ultracommerce.core.order.domain.DiscreteOrderItem;
import com.ultracommerce.core.order.domain.Order;
import com.ultracommerce.core.order.domain.OrderItem;
import com.ultracommerce.core.order.domain.OrderItemAttribute;
import com.ultracommerce.core.order.domain.OrderItemAttributeImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author bpolster
 */
@Service("bli18nUpdateCartServiceExtensionHandler")
public class i18nUpdateCartServiceExtensionHandler extends AbstractUpdateCartServiceExtensionHandler
        implements UpdateCartServiceExtensionHandler {

    protected static final Log LOG = LogFactory.getLog(i18nUpdateCartServiceExtensionHandler.class);

    protected boolean getClearCartOnLocaleSwitch() {
        return UCSystemProperty.resolveBooleanSystemProperty("clearCartOnLocaleSwitch");
    }

    @Resource(name = "ucCatalogService")
    protected CatalogService catalogService;

    @Resource(name = "ucUpdateCartServiceExtensionManager")
    protected UpdateCartServiceExtensionManager extensionManager;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.getHandlers().add(this);
        }
    }

    protected boolean getTranslationEnabled() {
        return UCSystemProperty.resolveBooleanSystemProperty("i18n.translation.enabled");
    }

    /**
     * If the locale of the cart does not match the current locale, then this extension handler will
     * attempt to translate the order items.  
     * 
     * The property "clearCartOnLocaleSwitch" can be set to true if the implementation desires to 
     * create a new cart when the locale is switched (3.0.6 and prior behavior).
     * 
     * @param cart
     * @param resultHolder
     * @return
     */
    public ExtensionResultStatusType updateAndValidateCart(Order cart, ExtensionResultHolder resultHolder) {
        if (UltraRequestContext.hasLocale() && getTranslationEnabled()) {
            UltraRequestContext brc = UltraRequestContext.getUltraRequestContext();
            if (!brc.getLocale().getLocaleCode().matches(cart.getLocale().getLocaleCode())) {
                if (LOG.isDebugEnabled()) {
                    String message = "The cart Locale [" + StringUtil.sanitize(cart.getLocale().getLocaleCode()) +
                            "] does not match the current locale [" + StringUtil.sanitize(brc.getLocale().getLocaleCode()) + "]";
                    LOG.debug(message);
                }

                if (getClearCartOnLocaleSwitch()) {
                    resultHolder.getContextMap().put("clearCart", Boolean.TRUE);
                } else {
                    fixTranslations(cart);
                    cart.setLocale(brc.getLocale());
                    resultHolder.getContextMap().put("saveCart", Boolean.TRUE);
                }
            }
        }
        return ExtensionResultStatusType.HANDLED_CONTINUE;
    }

    protected void fixTranslations(Order cart) {
        for (DiscreteOrderItem orderItem : cart.getDiscreteOrderItems()) {
            Sku sku = orderItem.getSku();
            translateOrderItem(orderItem, sku);
        }

        for (OrderItem orderItem : cart.getOrderItems()) {
            if (orderItem instanceof BundleOrderItem) {
                BundleOrderItem bundleItem = (BundleOrderItem) orderItem;
                Sku sku = bundleItem.getSku();
                translateOrderItem(orderItem, sku);
            }
        }
    }

    protected void translateOrderItem(OrderItem orderItem, Sku sku) {
        if (sku != null) {
            orderItem.setName(sku.getName());
            if (sku.getProductOptionValues() != null) {
                for (ProductOptionValue optionValue : sku.getProductOptionValues()) {
                    String key = optionValue.getProductOption().getAttributeName();
                    OrderItemAttribute attr = orderItem.getOrderItemAttributes().get(key);
                    if (attr != null) {
                        attr.setValue(optionValue.getAttributeValue());
                    } else {
                        OrderItemAttribute attribute = new OrderItemAttributeImpl();
                        attribute.setName(key);
                        attribute.setValue(optionValue.getAttributeValue());
                        attribute.setOrderItem(orderItem);
                        orderItem.getOrderItemAttributes().put(key, attribute);
                    }
                }
            }
        }
    }
}
