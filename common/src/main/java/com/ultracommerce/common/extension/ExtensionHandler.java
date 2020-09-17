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
package com.ultracommerce.common.extension;



/**
 * <p>An extension handler represents a generic pattern used in UltraCommerce when an out-of-box service
 * with complex logic provides implementation hooks.</p>
 * 
 * <p>The pattern is primarily used internally by Ultra as a mechanism to provide extension points for 
 * Ultra modules.</p>
 * 
 * <p>Consumers of UltraCommerce framework typically would not need to use this pattern and instead would opt. 
 * for more typical extension patterns including overriding or extending the actual component for which 
 * alternate behavior is desired.</p>
 * 
 * <p>ExtensionHandler api methods should always return an instance of {@link ExtensionResultStatusType} and will usually
 * extend from {@link AbstractExtensionHandler}</p>
 * 
 * <p>In order to associate an {@link ExtensionHandler) with an {@link ExtensionManager}, each handler should have an @PostConstruct
 * override and associate itself with the manager:</p>
 * 
 * <pre>
 *  {@code
 *    {@literal @}Resource(name = "ucSomeExtensionManager")
 *    protected ExtensionManager extensionManager;
 *
 *    {@literal @}PostConstruct
 *    public void init() {
 *       if (isEnabled()) {
 *           extensionManager.registerHandler(this);
 *       }
 *    }
 *  }
 * </pre>
 * 
 * 
 * @author bpolster
 * @see {@link AbstractExtensionHandler}
 */
public interface ExtensionHandler {

    /**
     * Determines the priority of this extension handler.
     * @return
     */
    public int getPriority();

    /**
     * If false, the ExtensionManager should skip this Handler.
     * @return
     */
    public boolean isEnabled();
}
