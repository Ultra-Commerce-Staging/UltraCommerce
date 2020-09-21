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
package com.ultracommerce.common.jmx;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * The MetadataNamingStrategy provided by Spring does not allow the usage of JDK dynamic proxies. However, several
 * of our services are AOP proxied for the sake of transactions, and the default behavior is to use JDK dynamic proxies for this.
 * It is possible to cause Spring to use CGLIB proxies instead via configuration, but this causes problems when it is desireable
 * or necessary to use constructor injection for the service definition, since CGLIB proxies require a default, no argument
 * constructor.
 * 
 * This class enhances the behavior of the Spring implementation to retrieve the rootId object inside the proxy for the sake of
 * metadata retrieval, thereby working around these shortcomings.
 * 
 * @author jfischer
 *
 */
public class MetadataNamingStrategy extends org.springframework.jmx.export.naming.MetadataNamingStrategy {

    public ObjectName getObjectName(Object managedBean, String beanKey) throws MalformedObjectNameException {
        managedBean = AspectUtil.exposeRootBean(managedBean);
        return super.getObjectName(managedBean, beanKey);
    }

}
