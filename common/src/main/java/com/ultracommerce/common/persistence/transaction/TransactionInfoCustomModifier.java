/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2017 Ultra Commerce
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
package com.ultracommerce.common.persistence.transaction;

import org.springframework.core.Ordered;

/**
 * Allows further customization of {@link TransactionInfo} instances upon transaction begin. Implementations can
 * modify existing values or add new ones. This is useful for tracking RDBMS specific attributes - see
 * {@link TransactionInfo#additionalParams}.
 *
 * @author Jeff Fischer
 */
public interface TransactionInfoCustomModifier extends Ordered {

    void modify(TransactionInfo info);

}
