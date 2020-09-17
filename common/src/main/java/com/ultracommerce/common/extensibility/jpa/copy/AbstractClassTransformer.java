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
package com.ultracommerce.common.extensibility.jpa.copy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

/**
 * This provides a useful mechanism to pre-load/initialize classes that are required by a child class during class transformation, 
 * but that may not have been loaded or initialized by the JVM.
 * 
 * @author Kelly Tisdell
 *
 */
public abstract class AbstractClassTransformer implements InitializingBean {

	protected static final Set<String> alreadyLoadedClasses = new HashSet<String>();
	protected List<String> preLoadClassNamePatterns = new ArrayList<String>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (preLoadClassNamePatterns != null && ! preLoadClassNamePatterns.isEmpty()) {
			synchronized (alreadyLoadedClasses) {
				for (String className : preLoadClassNamePatterns) {
					try {
						if (!alreadyLoadedClasses.contains(className)) {
							Class.forName(className);
							alreadyLoadedClasses.add(className);
						}
					} catch (ClassNotFoundException e) {
						throw new RuntimeException("Unable to force load class with name " + className + " in the DirectCopyClassTransformer", e);
					}
				}
			}
		}
	}
	
	/**
	 * Fully qualified list of class names to pre-load
	 * 
	 * @param fullyQualifiedClassNames
	 */
	public void setPreLoadClassNamePatterns(List<String> fullyQualifiedClassNames) {
    	this.preLoadClassNamePatterns = fullyQualifiedClassNames;
    }
}
